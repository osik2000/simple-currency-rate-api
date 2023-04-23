package pl.pawelosinski.dynatrace.nbp.task.backend.model.table;

import pl.pawelosinski.dynatrace.nbp.task.backend.model.rate.RateA;

import java.util.List;

public class TableA extends Table {

    private List<RateA> rates;

    public List<RateA> getRates() {
        return rates;
    }

    public void setRates(List<RateA> rates) {
        this.rates = rates;
    }

    public TableA() {
        super();
    }

    public TableA(String table, String currency, String code, List<RateA> rates) {
        super(table, currency, code);
        this.rates = rates;
    }
}
