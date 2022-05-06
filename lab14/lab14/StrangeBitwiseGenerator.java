package lab14;

import lab14lib.Generator;

public class StrangeBitwiseGenerator implements Generator {
    private int period;
    private int state;

    public StrangeBitwiseGenerator(int period) {
        this.period = period;
        state = 0;
    }

    @Override
    public double next() {
        state = state + 1;
        int weirdState = state & (state >> 3) % period;
        return 2.0 / (period - 1) * weirdState - 1;
    }
}
