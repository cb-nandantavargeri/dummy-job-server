package app.jobs.db;

import app.jobs.dao.SchJobs;
import app.jobs.jobs.JobStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DBUtil {

    /**
     * Gets a list of the scheduled jobs and updates their status to PICKED
     * @param limit Max number of jobs to be picked at a time
     * @return List of jobs to be sent to queue for execution
     */
    public static List<SchJobs> getScheduledJobsAndUpdateStatus(int limit) throws SQLException {
        Long now = Instant.now().getEpochSecond();

        String selectQuery =
                "SELECT * " +
                "FROM sch_jobs " +
                "WHERE status = 'SCHEDULED' AND scheduled_at <= ? " +
                "ORDER BY scheduled_at ASC " +
                "LIMIT ? " +
                "FOR UPDATE SKIP LOCKED";

        String setJobsAsPickedQuery =
                "UPDATE sch_jobs " +
                "SET status = 'PICKED', updated_at = ? " +
                "WHERE id = ?";

        try (Connection conn = ConnPool.getConnection()) {
            conn.setAutoCommit(false);

            List<SchJobs> pickedJobs = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement(selectQuery)) {
                ps.setLong(1, now);
                ps.setLong(2, limit);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        SchJobs job = getSchJobFromRs(rs);
                        pickedJobs.add(job);
                    }
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(setJobsAsPickedQuery)) {
                for (SchJobs job: pickedJobs) {
                    ps.setLong(1, now);
                    ps.setLong(2, job.getId());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conn.commit();
            return pickedJobs;
        }
    }

    /**
     * If record in sch_jobs has status PICKED, it will be updated to RUNNING
     * @param id Job id to be updated
     */
    public static void updateJobStatusToRunningFromPicked(Long id) throws SQLException {
        String updateQuery =
                "UPDATE sch_jobs " +
                "SET status = 'RUNNING', updated_at = ? " +
                "WHERE id = ?";

        try (Connection conn = ConnPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(updateQuery)) {
            ps.setLong(1, Instant.now().getEpochSecond());
            ps.setLong(2, id);
            ps.executeUpdate();
        }
    }

    /**
     * Updates status of the job by id to the given status
     * @param id Job id
     * @param status Status to be updated
     */
    public static void updateJobStatus(Long id, JobStatus status) throws SQLException {
        String updateQuery =
                "UPDATE sch_jobs " +
                "SET status = ?, updated_at = ? " +
                "WHERE id = ?";

        try (Connection conn = ConnPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(updateQuery)) {
            ps.setString(1, status.name());
            ps.setLong(2, Instant.now().getEpochSecond());
            ps.setLong(3, id);
            ps.executeUpdate();
        }
    }

    public static SchJobs getSchJobFromRs(ResultSet rs) throws SQLException {
        return new SchJobs(
                rs.getLong("id"),
                rs.getLong("site_id"),
                rs.getString("job_type"),
                rs.getString("status"),
                rs.getLong("created_at"),
                rs.getLong("updated_at"),
                rs.getLong("scheduled_at")
                );
    }
}
