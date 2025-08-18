package app.jobs.dao;

public class SchJobs {

    private Long id;
    private Long siteId;
    private String jobType;
    private String status;
    private Long createdAt;
    private Long updatedAt;
    private Long scheduledAt;

    public SchJobs(Long id, Long siteId, String jobType, String status, Long createdAt, Long updatedAt, Long scheduledAt) {
        this.id = id;
        this.siteId = siteId;
        this.jobType = jobType;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.scheduledAt = scheduledAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(Long scheduledAt) {
        this.scheduledAt = scheduledAt;
    }
}
