package pl.pawelosinski.dynatrace.nbp.task.model;

public class MinMaxRate {
    private Rate min;
    private Rate max;

    public MinMaxRate() {
    }

    public MinMaxRate(Rate min, Rate max) {
        this.min = min;
        this.max = max;
    }

    public Rate getMin() {
        return min;
    }

    public void setMin(Rate min) {
        this.min = min;
    }

    public Rate getMax() {
        return max;
    }

    public void setMax(Rate max) {
        this.max = max;
    }
}
