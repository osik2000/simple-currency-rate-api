package pl.pawelosinski.dynatrace.nbp.task.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.pawelosinski.dynatrace.nbp.task.backend.model.CurrencyRateTable;
import pl.pawelosinski.dynatrace.nbp.task.backend.model.MinMaxRate;
import pl.pawelosinski.dynatrace.nbp.task.backend.model.Rate;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.lang.Math.abs;

@Service
public class CurrencyService {

    private final RestTemplate restTemplate;

    @Autowired
    public CurrencyService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public CurrencyRateTable getRateFromDay(String currency, String date) throws Exception {
        CurrencyRateTable currencyRateTable;
        String url = "http://api.nbp.pl/api/exchangerates/rates/a/" + currency + "/" + date + "/?format=json";
        System.out.println(url);

        currencyRateTable = restTemplate.getForObject(url, CurrencyRateTable.class);
        if (currencyRateTable == null) {
            throw new Exception("Failed to retrieve data from API");
        }

        return currencyRateTable;

    }

    public Optional<MinMaxRate> getMinMaxFromQuotations(int n, String currency) throws Exception {
        CurrencyRateTable currencyRateTable;
        MinMaxRate minMaxRate;
        List<Rate> rates;

        String url = "http://api.nbp.pl/api/exchangerates/rates/a/" + currency + "/last/" + n;
        currencyRateTable = restTemplate.getForObject(url, CurrencyRateTable.class);
        if (currencyRateTable == null) {
            throw new Exception("Failed to retrieve data from API");
        }
        rates = currencyRateTable.getRates();
        if (!rates.isEmpty()) {

            minMaxRate = new MinMaxRate(
                    rates.stream().min(Comparator.comparing(Rate::getMid)).get(),
                    rates.stream().max(Comparator.comparing(Rate::getMid)).get()
            );
            return Optional.of(minMaxRate);
        }

        return Optional.empty();
    }

    public Optional<Rate> getDifferenceFromQuotations(int n, String currency) throws Exception {
        CurrencyRateTable currencyRateTable;
        Rate rate; // rate with major difference
        List<Rate> rates;

        String url = "http://api.nbp.pl/api/exchangerates/rates/c/" + currency + "/last/" + n;
        currencyRateTable = restTemplate.getForObject(url, CurrencyRateTable.class);
        if (currencyRateTable == null) {
            throw new Exception("Failed to retrieve data from API");
        }
        rates = currencyRateTable.getRates();
        if (!rates.isEmpty()) {
            rate = rates.stream().max(Comparator.comparing(v -> abs(v.getBid() - v.getAsk()))).get();
            return Optional.of(rate);
        }

        return Optional.empty();
    }

}
