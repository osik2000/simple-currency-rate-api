package pl.pawelosinski.dynatrace.nbp.task.backend.model.table;

import pl.pawelosinski.dynatrace.nbp.task.backend.model.rate.RateC;

import java.util.List;

public class TableC extends Table {

    private List<RateC> rates;

    public List<RateC> getRates() {
        return rates;
    }

    public void setRates(List<RateC> rates) {
        this.rates = rates;
    }

    public TableC() {
        super();
    }

    public TableC(String table, String currency, String code, List<RateC> rates) {
        super(table, currency, code);
        this.rates = rates;
    }
}
