package app.jobs.fw;

import app.jobs.db.DBUtil;
import app.jobs.jobs.Job;
import app.jobs.jobs.JobStatus;

import java.sql.SQLException;

/**
 * Runs a given job and makes necessary status updates
 */
public class Executor {

    private final Long jobId;
    private final String jobType;

    private static final String JOBS_PACKAGE = "app.jobs.jobs";

    public Executor(Long jobId, String jobType) {
        System.out.println("[EXECUTOR] Initializing executor for job id: " + jobId);
        this.jobId = jobId;
        this.jobType = jobType;
    }

    public void runJob() {
        try {
            String fqn = JOBS_PACKAGE + "." + jobType; // e.g., app.jobs.jobs.AddNumbersJob
            Class<?> clazz = Class.forName(fqn);

            if (!Job.class.isAssignableFrom(clazz)) {
                throw new IllegalArgumentException(fqn + " does not implement Job");
            }

            Job job = (Job) clazz.getDeclaredConstructor().newInstance();
            System.out.println("[EXECUTOR] Running job " + jobType + " with id " + jobId);
            DBUtil.updateJobStatusToRunningFromPicked(jobId);
            job.execute();
            System.out.println("[EXECUTOR] Successfully ran job, updating status to SUCCESS");
            updateJobStatus(JobStatus.SUCCESS);
        } catch (SQLException e) {
            System.err.println("[EXECUTOR] SQL exception occurred when executing the job:\n" + e.getMessage());
            updateJobStatus(JobStatus.FAILED);
        } catch (Exception e) {
            System.err.println("[EXECUTOR] Exception occurred while running job:\n" + e.getMessage());
            System.err.println("[EXECUTOR] Updating job status to FAILED");
            updateJobStatus(JobStatus.FAILED);
        }
    }

    private void updateJobStatus(JobStatus jobStatus) {
        try {
            DBUtil.updateJobStatus(jobId, jobStatus);
        } catch (SQLException ex) {
            System.err.println("[EXECUTOR] Exception while updating job status to " +
                    jobStatus.name() + "for job " + jobId);
        }
    }
}
