package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import java.io.*;

public class Game implements Serializable {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private static World world;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
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
            long seed = getSeed(input);
            world = new World(WIDTH, HEIGHT, seed);
        }
        // get player's position
        String playerPath = getPlayerPath(input);
        world.movePlayer(playerPath);
        // save the world
        if (input.charAt(input.length() - 2) == ':') {
            saveWorld(world);
        }

        return world.tiles;
    }

    private long getSeed(String input) {
        long seed = 0;
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

    private void saveWorld(World world) {
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
