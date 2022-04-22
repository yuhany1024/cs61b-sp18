package hw4.puzzle;
import edu.princeton.cs.algs4.Queue;

public class Board implements WorldState {
    private int[][] tiles;
    private int N;
    private int zeroAtI, zeroAtJ;

    public Board(int[][] tiles) {
        N = tiles.length;
        this.tiles = copyArray(tiles);
    }

    public int tileAt(int i, int j) {
        if (i >= 0 && i < N && j >= 0 && j < N) {
            return tiles[i][j];
        } else {
            throw new java.lang.IndexOutOfBoundsException();
        }
    }

    public int size() {
        return N;
    }

    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == 0) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = 0;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = 0;
                }
            }
        }
        return neighbors;
    }

    public int hamming() {
        int nWrongs = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (!(i == N - 1 && j == N - 1)) {
                    if (tiles[i][j] != 0 && tiles[i][j] != i * N + j + 1) {
                        nWrongs += 1;
                    }
                }
            }
        }
        return nWrongs;
    }

    public int manhattan() {
        int dist = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] != 0) {
                    int corretI = (tiles[i][j] - 1) / N;
                    int corretJ = (tiles[i][j] - 1) % N;
                    dist += Math.abs(i - corretI) + Math.abs(j - corretJ);
                }
            }
        }
        return dist;
    }

    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    public boolean equals(Object y) {
        if (y != null && y.getClass() == this.getClass()) {
            Board b = (Board) y;
            return b.size() == this.size()
                    && isEqual(b.tiles, this.tiles);
        } else {
            return false;
        }
    }

    public int hashCode() {
        int sum = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                sum = (sum + tiles[i][j]) * 31;
            }
        }
        return sum;
    }

    /** Returns the string representation of the board.
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

    private int[][] copyArray(int[][] arr) {
        int[][] newArr = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                newArr[i][j] = arr[i][j];
            }
        }
        return newArr;
    }

    private boolean isEqual(int[][] a, int[][] b) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (a[i][j] != b[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
