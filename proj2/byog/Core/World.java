package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.LinkedList;
import java.util.Random;
import java.util.List;
import java.io.Serializable;

public class World implements Serializable {
    protected final int WIDTH;
    protected final int HEIGHT;
    protected final Random RANDOM;
    private List<Room> roomList;
    private List<Hallway> hallwayList;
    private List<Position> wallList = new LinkedList<>();
    private Position door;
    private Position player;

    protected TETile[][] tiles;


    public World(int width, int height, long seed) {
        // initialize the world
        WIDTH = width;
        HEIGHT = height;
        RANDOM = new Random(seed);
        generateEmptyWorld();
        // generate rooms
        roomList = Room.generateRoomList(this);
        Room.sortRoomList(roomList);
        // generate hallways
        hallwayList = Hallway.generateHallways(roomList, RANDOM);
        // print floors
        printFloors(hallwayList);
        // generate and print walls
        generateWalls();
        // get Door and player
        generateDoor();
        generatePlayer();
    }

    private void generateEmptyWorld() {
        tiles = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    private void printFloors(List<Hallway> hallways) {
        for (Room room:  roomList) {
            fillRoomTiles(room);
        }

        for (Hallway hallway: hallways) {
            fillHallwayTiles(hallway);
        }
    }

    private void fillRoomTiles(Room room) {
        for (int x = room.pos.x; x < room.pos.x + room.width; x++) {
            for (int y = room.pos.y; y < room.pos.y + room.height; y++) {
                tiles[x][y] = Tileset.FLOOR;
            }
        }
    }

    private void fillHallwayTiles(Hallway h) {
        for (Position p: h.pixels) {
            tiles[p.x][p.y] = Tileset.FLOOR;
        }
    }

    private void generateWalls() {
        for (int x = 1; x < tiles.length - 1; x += 1) {
            for (int y = 1; y < tiles[0].length - 1; y += 1) {
                if (isWall(x, y)) {
                    tiles[x][y] = Tileset.WALL;
                    wallList.add(new Position(x, y));
                }
            }
        }
    }

    private boolean isWall(int x, int y) {
        if (tiles[x][y] == Tileset.FLOOR) {
            return false;
        }

        return tiles[x - 1][y] == Tileset.FLOOR
                || tiles[x + 1][y] == Tileset.FLOOR
                || tiles[x][y - 1] == Tileset.FLOOR
                || tiles[x - 1][y + 1] == Tileset.FLOOR
                || tiles[x - 1][y - 1] == Tileset.FLOOR
                || tiles[x - 1][y + 1] == Tileset.FLOOR
                || tiles[x + 1][y - 1] == Tileset.FLOOR
                || tiles[x + 1][y + 1] == Tileset.FLOOR;
    }

    private void generateDoor() {
        while (true) {
            int i = RANDOM.nextInt(wallList.size());
            Position wallPos = wallList.get(i);
            int x = wallPos.x;
            int y = wallPos.y;
            if (tiles[x - 1][y] == Tileset.FLOOR || tiles[x + 1][y] == Tileset.FLOOR
                    || tiles[x][y - 1] == Tileset.FLOOR || tiles[x][y + 1] == Tileset.FLOOR) {
                tiles[x][y] =  Tileset.LOCKED_DOOR;
                return;
            }
        }
    }

    private void generatePlayer() {
        int i = RANDOM.nextInt(roomList.size());
        Room room = roomList.get(i);
        int x = RANDOM.nextInt(room.width) + room.pos.x;
        int y = RANDOM.nextInt(room.height) + room.pos.y;
        player = new Position(x, y);
        tiles[x][y] = Tileset.PLAYER;
    }

    public void movePlayer(String path) {
        int nextX = player.x;
        int nextY = player.y;
        for (int i = 0; i < path.length(); i++) {
            switch (path.charAt(i)) {
                case 'w':
                    nextY += 1;
                    break;
                case 'a':
                    nextX -= 1;
                    break;
                case 's':
                    nextY -= 1;
                    break;
                case 'd':
                    nextX += 1;
                    break;
                default:
            }
            if (tiles[nextX][nextY].character() == 183 || tiles[nextX][nextY].character() == 9608) {
                tiles[player.x][player.y] =  Tileset.FLOOR;
                player.x = nextX;
                player.y = nextY;
                if (tiles[nextX][nextY].character() == 183) {
                    tiles[player.x][player.y] =  Tileset.PLAYER;
                } else {
                    tiles[player.x][player.y] =  Tileset.UNLOCKED_DOOR;
                    return;
                }

            }
        }
    }
}
