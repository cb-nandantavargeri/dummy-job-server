package app.jobs.jobs;

import java.util.concurrent.ThreadLocalRandom;

public class AddNumbersJob implements Job {
    @Override
    public void execute() throws Exception {
        int n = ThreadLocalRandom.current().nextInt(50, 200);
        long sum = 0;
        for (int i = 1; i <= n; i++) sum += i;
        System.out.println("AddNumbersJob: sum(1.." + n + ") = " + sum);
    }
}
