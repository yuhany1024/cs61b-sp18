package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Font;
import java.awt.Color;
import java.io.Serializable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

public class Game implements Serializable {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private static World world;
    private String seedInput = "";
    private long seed;
    private char lastKey = ' ';
    private char currentKey = ' ';

    private String infoTop = " ";
    private String infoBottom = " ";

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        initializeMenu();
        drawMainMenu();
        char action = getUserAction();
        if (action == 'n') {
            getSeedInput();
            seed = Integer.parseInt(seedInput);
            world = new World(WIDTH, HEIGHT, seed);
        } else if (action == 'l') {
            world = loadWorld();
        } else if (action == 'q') {
            System.exit(0);
        }

        ter.initialize(WIDTH, HEIGHT);
        startGame();
    }

    private void initializeMenu() {
        StdDraw.setCanvasSize(WIDTH  * 16, HEIGHT * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, WIDTH * 16);
        StdDraw.setYscale(0, HEIGHT * 16);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
    }

    private void drawMainMenu() {
        StdDraw.clear();
        StdDraw.clear(Color.black);

        Font bigFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH / 2  * 16, HEIGHT / 2 * 16 + 7, "CS61B: THE GAME");

        Font smallFont = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(smallFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH /  2 * 16, 135, "New Game (N)");
        StdDraw.textLeft(WIDTH / 2 *  16 + 120, 135, "Seed: " + seedInput);
        StdDraw.text(WIDTH / 2 * 16, 95, "Load Game (L)");
        StdDraw.text(WIDTH / 2 *  16, 55, "Quit (Q)");
        StdDraw.show();
    }

    public char getUserAction() {
        char key =  ' ';
        while (key != 'n' && key !=  'l' && key != 'q') {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            key = StdDraw.nextKeyTyped();
            key = Character.toLowerCase(key);
            if (key == 'n' || key == 'l' || key == 'q') {
                break;
            }
        }
        return key;
    }

    public void getSeedInput() {
        while (true) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            key  = Character.toLowerCase(key);

            if (key ==  's') {
                return;
            }
            if (!Character.isDigit(key)) {
                continue;
            }
            seedInput += String.valueOf(key);
            drawMainMenu();
        }
    }


    private void startGame() {
        // initialize the world
        boolean gameWin = false;
        String quitInfo;

        infoBottom = "Start!";
        renderWorld();

        while (!gameWin) {
            checkCursor();
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            lastKey = currentKey;
            currentKey = StdDraw.nextKeyTyped();
            currentKey  = Character.toLowerCase(currentKey);
            if (currentKey  == ':') {
                infoBottom = ":";
                renderWorld();
            }
            if (currentKey  == 'q' && lastKey == ':') {
                infoBottom = ":q";
                renderWorld();
                saveWorld();
                playWithKeyboard();
            }
            if  (currentKey == 's' || currentKey ==  'a' || currentKey == 'd'
                    ||  currentKey == 'w') {
                gameWin = world.movePlayer(Character.toString(currentKey));
                infoBottom = Character.toString(currentKey);
                renderWorld();
            }

        }
        if (gameWin) {
            infoBottom = "WIN!";
            renderWorld();
        }
    }

    private void checkCursor() {
        int x  = (int) StdDraw.mouseX();
        int y = (int) StdDraw.mouseY();
        TETile tile = world.tiles[x][y];

        if (tile.description() != infoTop)  {
            infoTop =  tile.description();
            renderWorld();
        }
    }

    private void  renderWorld() {
        StdDraw.clear();
        StdDraw.clear(Color.black);

        Font font1 = new Font("Monaco", Font.BOLD, 14);
        StdDraw.setFont(font1);
        StdDraw.setPenColor(Color.white);

        ter.renderFrame(world.tiles);

        Font font2 = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(font2);
        StdDraw.setPenColor(Color.white);
        StdDraw.textLeft(1, 2, infoBottom);

        Font font3 = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(font3);
        StdDraw.setPenColor(Color.white);
        StdDraw.textLeft(1, HEIGHT -  3, infoTop);

        StdDraw.show();
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        input = input.toLowerCase();
        // initialize the world
        if (input.charAt(0) == 'l') { // load the world
            world = loadWorld();
        } else {
            seed = getSeed(input);
            world = new World(WIDTH, HEIGHT, seed);
        }
        // get player's position
        String playerPath = getPlayerPath(input);
        world.movePlayer(playerPath);
        // save the world
        if (input.charAt(input.length() - 2) == ':') {
            saveWorld();
        }

        return world.tiles;
    }

    private long getSeed(String input) {
        seed = 0;
        for (int i = 0; i < input.length(); i += 1) {
            if (Character.isDigit(input.charAt(i))) {
                seed = 10 * seed + Long.parseLong("" + input.charAt(i));
            }
        }
        return seed;
    }

    private String getPlayerPath(String input) {
        if (input.charAt(0) == 'n') {
            int start = 1;
            while (start < input.length() && input.charAt(start) != 's') {
                start += 1;
            }
            if (input.charAt(input.length() - 2) == ':') {
                return input.substring(start + 1, input.length() - 2);
            } else {
                return input.substring(start + 1, input.length());
            }
        } else {
            if (input.charAt(input.length() - 2) == ':') {
                return input.substring(1, input.length() - 2);
            } else {
                return input.substring(1, input.length());
            }
        }

    }

    private void saveWorld() {
        File f = new File("./world.txt");
        try {
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(world);
            System.out.println("Save the world in world.txt!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private World loadWorld() {
        File f = new File("./world.txt");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                return (World) os.readObject();
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }
        throw new RuntimeException("no file exists");
    }
}
