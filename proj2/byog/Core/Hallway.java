package byog.Core;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.io.Serializable;

public class Hallway implements Serializable{
    // generate  L-shaped hallways
    public List<Position> pixels;

    public Hallway(Position start, Position end) {
        pixels = new LinkedList<>();
        int xmin = Math.min(start.x, end.x);
        int xmax = Math.max(start.x, end.x);
        int ymin = Math.min(start.y, end.y);
        int ymax = Math.max(start.y, end.y);

        for (int x = xmin; x <= xmax; x++) {
            pixels.add(new Position(x, start.y));
        }

        for (int y = ymin; y <= ymax; y++) {
            pixels.add(new Position(end.x, y));
        }
    }

    public static List<Hallway> generateHallways(List<Room> roomList, Random random) {
        List<Hallway> hallways = new LinkedList<>();
        for (int i = 1; i < roomList.size(); i++) {
            Room room1 = roomList.get(i-1);
            Room room2 = roomList.get(i);
            Position start = randomPos(room1,random);
            Position end = randomPos(room2, random);
            hallways.add(new Hallway(start, end));
        }
        return hallways;
    }

    private static Position randomPos(Room room, Random random) {
        // generate a random position on the room's border
        int flag = random.nextInt(4);
        int x, y;
        switch (flag) {
            case 0:
                y = random.nextInt(room.pos.y, room.pos.y+room.height);
                return new Position(room.pos.x, y);
            case 1:
                x = random.nextInt(room.pos.x, room.pos.x+room.width);
                return new Position(x, room.pos.y);
            case 2:
                y = random.nextInt(room.pos.y, room.pos.y+room.height);
                return new Position(room.pos.x+ room.width-1, y);
            case 3:
                x = random.nextInt(room.pos.x, room.pos.x+room.width);
                return new Position(x, room.pos.y+ room.height-1);
            default:
                return null;
        }
    }

}
