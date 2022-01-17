package byog.Core;
import java.io.Serializable;

public class Position implements Serializable {
    protected int x;
    protected int y;
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
