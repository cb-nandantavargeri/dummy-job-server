package app.jobs.jobs;

@FunctionalInterface
public interface Job {
    void execute() throws Exception;
}
