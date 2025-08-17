package app.jobs.jobs;

import java.util.concurrent.ThreadLocalRandom;

public class SleepJob implements Job {
    @Override
    public void execute() throws Exception {
        long ms = ThreadLocalRandom.current().nextLong(200, 1200);
        System.out.println("SleepJob: sleeping " + ms + " ms");
        Thread.sleep(ms);
        System.out.println("SleepJob: woke up");
    }
}
