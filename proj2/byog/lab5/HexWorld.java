package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    /**
     *
     * @param px x coordinate of the upper left corner of the hexagon
     * @param py y coordinate of the upper left corner of the hexagon
     */
    public static void addHexagon(int px, int py, int size) {
        printEmptyLine(py);
        for (int line = 1; line < 2 * size + 1; line += 1) {
            if (line > size) {
                printSpaces(px - 2 * size + line);
            } else {
                printSpaces(px - line + 1);
            }
            printA(calcN(line, size));
        }
    }

    private static void printEmptyLine(int size) {
        for (int i = 0; i < size; i += 1) {
            System.out.println();
        }
    }

    private static void printSpaces(int size) {
        for (int i = 0; i < size; i += 1) {
            System.out.print(" ");
        }
    }

    private static int calcN  (int line, int size) {
        if (line > size) {
            return calcN(2*size-line+1, size);
        } else {
            return size + (line - 1) * 2;
        }
    }

    private static void printA(int size) {
        for (int i = 0; i < size; i += 1) {
            System.out.print("a");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        addHexagon(10, 1,3);
    }
}
