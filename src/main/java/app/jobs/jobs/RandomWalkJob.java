package app.jobs.jobs;

import java.util.concurrent.ThreadLocalRandom;

public class RandomWalkJob implements Job {
    @Override
    public void execute() throws Exception {
        int steps = ThreadLocalRandom.current().nextInt(500, 2000);
        int pos = 0;
        for (int i = 0; i < steps; i++) pos += ThreadLocalRandom.current().nextBoolean() ? 1 : -1;
        System.out.println("RandomWalkJob: steps=" + steps + ", finalPosition=" + pos);
    }
}

