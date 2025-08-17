package app.jobs.fw;

import app.jobs.queue.Queue;

import java.util.concurrent.RejectedExecutionException;

/**
 * Polls the queue for jobs to be executed. Once found, starts the
 * executor thread to execute the job.
 */
public class Poller {

    public void pollForJobs() {
        System.out.println("[POLLER] Polling for jobs...");
        String message = Queue.receiveMessage();
        if (message != null) {
            String[] parsedMessage = message.split("-");
            Long jobId = Long.valueOf(parsedMessage[0]);
            String jobType = parsedMessage[1];
            runJob(jobId, jobType, message);
        }
    }

    private void runJob(Long jobId, String jobType, String originalMessage) {
        System.out.println("[POLLER] Trying to run job " + jobType + " with id " + jobId);
        Executor jobExecutor = new Executor(jobId, jobType);
        Runnable r = jobExecutor::runJob;
        try {
            ThreadPool.threadPool.execute(r);
        } catch (RejectedExecutionException e) {
            System.out.println("[POLLER] Thread pool is occupied, sending message back to queue..");
            Queue.sendMessage(originalMessage);
        }
    }
}
