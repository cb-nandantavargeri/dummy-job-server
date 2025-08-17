package app.jobs.jobs;

import java.util.concurrent.ThreadLocalRandom;

public class RandomPrimeJob implements Job {
    @Override
    public void execute() throws Exception {
        int start = ThreadLocalRandom.current().nextInt(10_000, 50_000);
        int p = start;
        while (!isPrime(p)) p++;
        System.out.println("RandomPrimeJob: next prime >= " + start + " is " + p);
    }

    private boolean isPrime(int x) {
        if (x < 2) return false;
        if (x % 2 == 0) return x == 2;
        for (int d = 3; d * d <= x; d += 2) {
            if (x % d == 0) return false;
        }
        return true;
    }
}
