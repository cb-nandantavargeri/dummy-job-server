package app.jobs.jobs;

import java.util.concurrent.ThreadLocalRandom;

public class MatrixMultiplyJob implements Job {
    @Override
    public void execute() throws Exception {
        int n = ThreadLocalRandom.current().nextInt(20, 40); // n x n
        double[][] A = rand(n), B = rand(n), C = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int k = 0; k < n; k++) {
                double aik = A[i][k];
                for (int j = 0; j < n; j++) {
                    C[i][j] += aik * B[k][j];
                }
            }
        }
        System.out.println("MatrixMultiplyJob: multiplied " + n + "x" + n + " matrices, C[0][0]=" + C[0][0]);
    }

    private double[][] rand(int n) {
        double[][] m = new double[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                m[i][j] = ThreadLocalRandom.current().nextDouble(-1.0, 1.0);
        return m;
    }
}
