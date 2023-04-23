package pl.pawelosinski.dynatrace.nbp.task.backend.model;

public class Rate {
    private String no;
    private String effectiveDate;
    private double mid;
    private double bid;
    private double ask;

    public Rate() {
    }

    public Rate(String no, String effectiveDate, double mid, double bid, double ask) {
        this.no = no;
        this.effectiveDate = effectiveDate;
        this.mid = mid;
        this.bid = bid;
        this.ask = ask;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public double getMid() {
        return mid;
    }

    public void setMid(double mid) {
        this.mid = mid;
    }

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }

    public double getAsk() {
        return ask;
    }

    public void setAsk(double ask) {
        this.ask = ask;
    }
}
