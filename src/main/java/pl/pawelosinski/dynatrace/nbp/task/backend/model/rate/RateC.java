package pl.pawelosinski.dynatrace.nbp.task.backend.model.rate;

public class RateC extends Rate{
    private double ask;
    private double bid;

    public double getAsk() {
        return ask;
    }

    public void setAsk(double ask) {
        this.ask = ask;
    }

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }
}
