package pl.pawelosinski.dynatrace.nbp.task.backend.model.table;

public class Table {
    private String table = "";
    private String currency = "";
    private String code = "";

    public Table() {
    }

    public Table(String table, String currency, String code) {
        this.table = table;
        this.currency = currency;
        this.code = code;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}

