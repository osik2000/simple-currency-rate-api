package pl.pawelosinski.dynatrace.nbp.task.backend.model.rate;

public class Rate {
    private String no;
    private String effectiveDate;

    public Rate() {
    }

    public Rate(String no, String effectiveDate, double mid, double bid, double ask) {
        this.no = no;
        this.effectiveDate = effectiveDate;

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

}
