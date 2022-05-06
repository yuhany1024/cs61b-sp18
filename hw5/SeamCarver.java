import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

public class SeamCarver {
    private Picture picture;

    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
    }

    public Picture picture() {
        // current picture
        return new Picture(picture);
    }

    public int width() {
        // width of current picture
        return picture.width();
    }

    public int height() {
        // height of current picture
        return picture.height();
    }

    public double energy(int x, int y) {
        // energy of pixel at column x and row y
        int w = width();
        int h = height();
        if (x < 0 || y < 0 || x >= w || y >= h) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        Color leftColor = picture.get((x - 1 + w) % w, y);
        Color rightColor = picture.get((x + 1) % w, y);
        Color upColor = picture.get(x, (y - 1 + h) % h);
        Color downColor = picture.get(x, (y + 1) % h);

        return colorGradientSquare(leftColor, rightColor)
                + colorGradientSquare(upColor,  downColor);
    }

    private double colorGradientSquare(Color color1, Color color2) {
        return Math.pow(color1.getRed() - color2.getRed(), 2)
                + Math.pow(color1.getGreen() - color2.getGreen(), 2)
                + Math.pow(color1.getBlue() - color2.getBlue(), 2);
    }

    private static Picture transpose(Picture picture) {
        int h = picture.height();
        int w = picture.width();
        Picture temp = new Picture(h, w);
        for (int i = 0; i < h; i += 1) {
            for (int j = 0; j < w; j += 1) {
                temp.set(i, j, picture.get(j, i));
            }
        }
        return temp;
    }

    public int[] findHorizontalSeam() {
        // sequence of indices for horizontal seam
        picture = transpose(picture);
        int[] seam = findVerticalSeam();
        picture = transpose(picture);
        return seam;
    }

    private int min(double[][] arr, int row, int col) {
        // find which pixel has  the lowest energy
        // (row, col),  (row,  col - 1), (row, col + 1)
        int res = (col - 1 >= 0 && arr[row][col - 1] < arr[row][col]) ? -1 : 0;
        double minEnergy = (col - 1 >= 0 && arr[row][col - 1] < arr[row][col])
                ? arr[row][col - 1] : arr[row][col];
        res = (col + 1 < width() && arr[row][col + 1] < minEnergy) ? 1 : res;
        return res;
    }

    private int argMin(double[] arr) {
        // return the index of the min element
        double minVal = Double.MAX_VALUE;
        int index = 0;
        for (int i = 0; i < arr.length; i++) {
            index = arr[i] < minVal ? i : index;
            minVal = arr[i] < minVal ? arr[i] : minVal;
        }
        return index;
    }

    public int[] findVerticalSeam() {
        // sequence of indices for vertical seam
        int w = width();
        int h = height();
        double[][] minEnergy = new double[h][w];
        int[][] path = new int[h][w];
        int[] seam = new int[h];
        for (int i = 0; i < w; i++) {
            minEnergy[0][i] = energy(i, 0);
            path[0][i] = i;
        }
        for (int i = 1; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int p = min(minEnergy, i - 1, j);
                minEnergy[i][j] = energy(j, i) + minEnergy[i - 1][j + p];
                path[i][j] = j + p;
            }
        }
        seam[h - 1] = argMin(minEnergy[h - 1]);
        for (int i = h - 2; i >= 0; i--) {
            seam[i] = path[i + 1][seam[i + 1]];
        }
        return seam;
    }

    public void removeHorizontalSeam(int[] seam) {
        // remove horizontal seam from picture
        picture = transpose(picture);
        removeVerticalSeam(seam);
        picture = transpose(picture);
    }

    public void removeVerticalSeam(int[] seam) {
        // remove vertical seam from picture
        if (!validSeam(seam)) {
            throw new java.lang.IllegalArgumentException();
        }
        picture = new Picture(SeamRemover.removeVerticalSeam(picture, seam));
    }

    private boolean validSeam(int[] seam) {
        int h = height();
        int w =  width();
        if (seam.length != h) {
            return false;
        }
        for (int i = 0; i < h; i++) {
            if (seam[i] < 0 || seam[i] >= w) {
                return false;
            }
            if (i > 0 && Math.abs(seam[i] - seam[i - 1]) > 1) {
                return false;
            }
        }
        return true;
    }

}
