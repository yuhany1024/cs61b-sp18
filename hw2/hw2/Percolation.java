package hw2;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private int N;
    private WeightedQuickUnionUF disjointSet;
    private WeightedQuickUnionUF disjointSetNoBW; // no backwash
    private int top;
    private int bottom;
    private int nOpenSites = 0;

    public  Percolation(int N) {
        //  create N-by-N grid, with all sites initially blocked
        if (N < 0) {
            throw new java.lang.IllegalArgumentException("N must be positive!");
        }
        this.N = N;
        grid = new boolean[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                grid[i][j] = false;
            }
        }

        disjointSet = new WeightedQuickUnionUF(N * N + 2);
        disjointSetNoBW = new WeightedQuickUnionUF(N * N + 1);
        top = N * N;
        bottom = N * N + 1;
    }

    public void open(int row, int col) {
        //open the site (row, col) if it is not open already
        if (!isValidPos(row, col)) {
            throw new java.lang.IndexOutOfBoundsException("Must input a valid position!");
        }
        if (!grid[row][col]) {
            grid[row][col] = true;
            nOpenSites += 1;
            if (isValidPos(row - 1, col) && grid[row - 1][col]) {
                disjointSet.union((row - 1) * N + col, row * N + col);
                disjointSetNoBW.union((row - 1) * N + col, row * N + col);
            }
            if (isValidPos(row + 1, col) && grid[row + 1][col]) {
                disjointSet.union((row + 1) * N + col, row * N + col);
                disjointSetNoBW.union((row + 1) * N + col, row * N + col);
            }
            if (isValidPos(row, col + 1) && grid[row][col + 1]) {
                disjointSet.union(row * N + col + 1, row * N + col);
                disjointSetNoBW.union(row * N + col + 1, row * N + col);
            }
            if (isValidPos(row, col - 1) && grid[row][col - 1]) {
                disjointSet.union(row * N + col - 1, row * N + col);
                disjointSetNoBW.union(row * N + col - 1, row * N + col);
            }
            if (row == 0) {
                disjointSet.union(col, top);
                disjointSetNoBW.union(col, top);
            }
            if (row == N - 1) {
                disjointSet.union((N - 1) * N + col, bottom);
            }
        }
    }

    public boolean isOpen(int row, int col) {
        // is the site (row, col) open?
        if (!isValidPos(row, col)) {
            throw new  java.lang.IndexOutOfBoundsException("Must input a valid position!");
        }
        return grid[row][col];
    }

    public boolean isFull(int row, int col) {
        // is the site (row, col) full?}
        if (!isValidPos(row, col)) {
            throw new java.lang.IndexOutOfBoundsException("Must input a valid position!");
        }

        return disjointSetNoBW.connected(top, row * N + col);
    }

    public int numberOfOpenSites() {
        // number of open sites
        return nOpenSites;
    }

    public boolean percolates() {
        // does the system percolate?
        return disjointSet.connected(top, bottom);
    }

    private boolean isValidPos(int row, int col) {
        return row >= 0 && row < N && col >= 0 && col < N;
    }

    public static void main(String[] args) {
        // use for unit testing (not required)}
        Percolation system = new Percolation(3);
        system.open(0, 1);
        system.open(1, 1);
        system.open(1, 2);
        System.out.println(system.isFull(1, 0));
    }

}
