import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {
    @Test
    public void testArrayDeque() {
        ArrayDequeSolution<Integer> sad0 = new ArrayDequeSolution<>();
        StudentArrayDeque<Integer> sad = new StudentArrayDeque<>();
        String msg = "";

        // Initialize the deque @source studentArrayDequeLauncher
        for (int i = 0; i < 100; i += 1) {
            int num = StdRandom.uniform(0,4);

            if (num == 0) {
                msg = msg + "addLast(" + i + ")\n";
                sad.addLast(i);
                sad0.addLast(i);
            } else if (num == 1) {
                msg = msg + "addFirst(" + i + ")\n";
                sad.addFirst(i);
                sad0.addFirst(i);
            } else if (num == 2) {
                if (!sad0.isEmpty()) {
                    msg = msg + "removeFirst()\n";
                    assertEquals(sad0.isEmpty(), sad.isEmpty());
                    Integer val = sad.removeFirst();
                    Integer val0 = sad0.removeFirst();
                    assertEquals(msg, val0, val);
                }
            } else if (num == 3) {
                if (!sad0.isEmpty()) {
                    msg = msg + "removeLast()\n";
                    assertEquals(sad0.isEmpty(), sad.isEmpty());
                    Integer val = sad.removeLast();
                    Integer val0 = sad0.removeLast();
                    assertEquals(msg, val0, val);
                }
            }

            assertEquals(sad0.size(), sad.size());
        }

        for (int i = 0; i<sad0.size(); i++) {
            msg = msg + "get()\n";
            assertEquals(msg, sad0.get(i), sad.get(i));
        }

    }

}


