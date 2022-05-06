package lab14;

import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator {
    private int period;
    private double factor;
    private int state;

    public AcceleratingSawToothGenerator(int period, double factor) {
        this.period = period;
        this.factor = factor;
        this.state = 0;
    }

    @Override
    public double next() {
        state = state + 1;
        if (state == period) {
            period = (int) Math.floor(period * factor);
            state = 0;
        }
        double tone = 2.0 / (period - 1) * state - 1;

        return tone;
    }
}
