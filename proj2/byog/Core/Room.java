package byog.Core;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.io.Serializable;

public class Room implements Comparable<Room>, Serializable {
    public Position pos;
    public int width;
    public int height;

    public Room(Position pos, int width, int height) {
        this.pos = pos;
        this.width = width;
        this.height = height;
    }

    @Override
    public int compareTo(Room r) {
        return this.pos.x - r.pos.x;
    }

    public static List<Room> generateRoomList(World world) {
        Random random = world.RANDOM;
        int nRoom = random.nextInt(30,70);
        List<Room> roomList = new LinkedList<>();
        for (int i =0; i < nRoom; i++) {
            Room room = randomRoom(world);
            roomList.add(room);
        }
        removeOverlappedRoom(roomList);
        return roomList;
    }

    private static Room randomRoom(World world) {
        Random random = world.RANDOM;
        int roomX = random.nextInt(3,world.WIDTH-8);
        int roomY = random.nextInt(3, world.HEIGHT-8);
        int roomWidth = random.nextInt(3, 7);
        int roomHeight = random.nextInt(3, 7);
        Position roomPos = new Position(roomX, roomY);
        return new Room(roomPos, roomWidth, roomHeight);
    }

    private static void removeOverlappedRoom(List<Room> roomList) {
        // check whether the room is overlapped with any room in the roomList
        // if it's overlapped, remove it.
        for (int i = 1; i < roomList.size(); i++) {
            for (int j = 0; j < i; j++) {
                Room room1 = roomList.get(i);
                Room  room2 = roomList.get(j);
                if (isOverlap(room1, room2)) {
                    roomList.remove(i);
                    i -= 1;
                    continue;
                }
            }
        }
    }

    private static boolean isOverlap(Room room1, Room room2) {
        return !(room1.pos.x + room1.width <= room2.pos.x
                || room2.pos.x + room2.width <= room1.pos.x
                || room1.pos.y + room1.height <= room2.pos.y
                || room2.pos.y + room2.height <= room1.pos.y);
    }

    public static void sortRoomList(List<Room> roomList) {
        Collections.sort(roomList);
    }

}
