package pl.pawelosinski.dynatrace.nbp.task.backend.model;

import pl.pawelosinski.dynatrace.nbp.task.backend.model.rate.RateA;

public class MinMaxRate {
    private RateA min;
    private RateA max;

    public MinMaxRate() {
    }

    public MinMaxRate(RateA min, RateA max) {
        this.min = min;
        this.max = max;
    }

    public RateA getMin() {
        return min;
    }

    public void setMin(RateA min) {
        this.min = min;
    }

    public RateA getMax() {
        return max;
    }

    public void setMax(RateA max) {
        this.max = max;
    }
}
