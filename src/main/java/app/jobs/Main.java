package app.jobs;

import app.jobs.db.ConnPool;
import app.jobs.fw.Picker;
import app.jobs.fw.Poller;
import app.jobs.fw.ThreadPool;
import app.jobs.queue.Queue;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        // Initialize thread pool for job executors
        ThreadPool.initThreadPool();

        ConnPool.initConnPool();
        Queue.initQueue();

        if ("true".equals(System.getenv("IS_PICKER"))) {
            Picker picker = new Picker();
            ScheduledExecutorService pickerThread = Executors.newSingleThreadScheduledExecutor();
            pickerThread.scheduleWithFixedDelay(() -> {
                try {
                    picker.pickJobs();
                } catch (Throwable t) {
                    System.err.println("[MAIN] Unhandled picker throwable: " + t.getMessage());
                }
            }, 0, 2, java.util.concurrent.TimeUnit.MINUTES);
        }

        Poller poller = new Poller();
        ScheduledExecutorService pollerThread = Executors.newSingleThreadScheduledExecutor();
        pollerThread.scheduleWithFixedDelay(() -> {
            try {
                poller.pollForJobs();
            } catch (Throwable t) {
                System.err.println("[MAIN] Unhandled poller throwable: " + t.getMessage());
            }
        }, 0, 2, java.util.concurrent.TimeUnit.MINUTES);
    }
}