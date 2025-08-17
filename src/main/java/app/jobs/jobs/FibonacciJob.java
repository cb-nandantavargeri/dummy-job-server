package app.jobs.jobs;

import java.util.concurrent.ThreadLocalRandom;

public class FibonacciJob implements Job {
    @Override
    public void execute() throws Exception {
        int n = ThreadLocalRandom.current().nextInt(20, 40);
        long a = 0, b = 1;
        for (int i = 0; i < n; i++) {
            long tmp = a + b;
            a = b;
            b = tmp;
        }
        System.out.println("FibonacciJob: F(" + n + ") = " + a);
    }
}
