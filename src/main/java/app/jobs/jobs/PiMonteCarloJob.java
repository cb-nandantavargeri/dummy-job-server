package app.jobs.jobs;

import java.util.concurrent.ThreadLocalRandom;

public class PiMonteCarloJob implements Job {
    @Override
    public void execute() throws Exception {
        int samples = ThreadLocalRandom.current().nextInt(50_000, 200_000);
        int inside = 0;
        for (int i = 0; i < samples; i++) {
            double x = ThreadLocalRandom.current().nextDouble();
            double y = ThreadLocalRandom.current().nextDouble();
            if (x * x + y * y <= 1.0) inside++;
        }
        double pi = 4.0 * inside / samples;
        System.out.printf("PiMonteCarloJob: samples=%d, pi≈%.6f%n", samples, pi);
    }
}
