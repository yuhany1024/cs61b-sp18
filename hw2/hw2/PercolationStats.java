package hw2;
import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {
    private double[] threshold;
    private int T;
    public PercolationStats(int N, int T, PercolationFactory pf) {
        // perform T independent experiments on an N-by-N grid
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("N and T must be positive!");
        }
        this.T = T;
        threshold = new double[T];
        for (int i = 0; i < T; i++) {
            Percolation percolation = pf.make(N);
            while (true) {
                int newSite = StdRandom.uniform(N * N);
                percolation.open(newSite / N, newSite % N);
                if (percolation.percolates()) {
                    break;
                }
            }
            threshold[i] = percolation.numberOfOpenSites() * 1.0 / N / N;
        }
    }

    public double mean() {
        // sample mean of percolation threshold
        return StdStats.mean(threshold);
    }

    public double stddev() {
        // sample standard deviation of percolation threshold
        return StdStats.stddev(threshold);
    }

    public double confidenceLow() {
        // low endpoint of 95% confidence interval
        return mean() - 1.96 * stddev() / Math.sqrt(T);
    }

    public double confidenceHigh() {
        // high endpoint of 95% confidence interval
        return mean() + 1.96 * stddev() / Math.sqrt(T);
    }
    
}

