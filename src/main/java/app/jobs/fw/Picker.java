package app.jobs.fw;

import app.jobs.dao.SchJobs;
import app.jobs.db.DBUtil;
import app.jobs.queue.Queue;

import java.sql.SQLException;
import java.util.List;

/**
 * Scans sch_jobs table for jobs to be scheduled. Once found, jobs are
 * sent to the queue and their status is updated
 */
public class Picker {
    public void pickJobs() {
        System.out.println("[PICKER] Scanning sch_jobs table...");
        try {
            List<SchJobs> pickedJobs = DBUtil.getScheduledJobsAndUpdateStatus(20);
            System.out.println("[PICKER] Found " + pickedJobs.size() + " jobs to be scheduled");
            for (SchJobs job: pickedJobs) {
                System.out.println("[PICKER] Sending job with id " + job.getId() + " to queue");
                String message = job.getId() + "-" + job.getJobType();
                Queue.sendMessage(message);
            }
        } catch (SQLException e) {
            System.err.println("[PICKER] Exception occurred while querying sch_jobs:\n" + e.getMessage());
        }
    }
}
