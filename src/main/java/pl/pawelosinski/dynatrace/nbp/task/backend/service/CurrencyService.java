package pl.pawelosinski.dynatrace.nbp.task.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.pawelosinski.dynatrace.nbp.task.backend.model.*;
import pl.pawelosinski.dynatrace.nbp.task.backend.model.rate.RateA;
import pl.pawelosinski.dynatrace.nbp.task.backend.model.rate.RateC;
import pl.pawelosinski.dynatrace.nbp.task.backend.model.table.TableA;
import pl.pawelosinski.dynatrace.nbp.task.backend.model.table.TableC;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.lang.Math.abs;

@Service
public class CurrencyService {

    private final String apiCoreUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public CurrencyService(String apiCoreUrl, RestTemplateBuilder builder) {
        this.apiCoreUrl = apiCoreUrl;
        this.restTemplate = builder.build();
    }

    public Optional<TableA> getRateFromDay(String currency, String date) throws Exception {
        TableA tableA;
        String url = apiCoreUrl + "/api/exchangerates/rates/a/" + currency + "/" + date + "/?format=json";

        tableA = restTemplate.getForObject(url, TableA.class);
        if (tableA == null) {
            throw new Exception("Failed to retrieve data from API");
        }

        return Optional.of(tableA);

    }

    public Optional<MinMaxRate> getMinMaxFromQuotations(int n, String currency) throws Exception {
        TableA tableA;
        MinMaxRate minMaxRate;
        List<RateA> rates;

        String url = apiCoreUrl + "/api/exchangerates/rates/a/" + currency + "/last/" + n + "/?format=json";
        tableA = restTemplate.getForObject(url, TableA.class);
        if (tableA == null) {
            throw new Exception("Failed to retrieve data from API");
        }
        rates = tableA.getRates();
        if (!rates.isEmpty()) {
            minMaxRate = new MinMaxRate(
                    rates.stream().min(Comparator.comparing(RateA::getMid)).orElse(null),
                    rates.stream().max(Comparator.comparing(RateA::getMid)).orElse(null)
            );
            return Optional.of(minMaxRate);
        }
        return Optional.empty();
    }

    public Optional<RateC> getDifferenceFromQuotations(int n, String currency) throws Exception {
        TableC tableC;
        RateC rate; // rate with major difference
        List<RateC> rates;

        String url = apiCoreUrl + "/api/exchangerates/rates/c/" + currency + "/last/" + n + "/?format=json";
        tableC = restTemplate.getForObject(url, TableC.class);
        if (tableC == null) {
            throw new Exception("Failed to retrieve data from API");
        }
        rates = tableC.getRates();
        if (!rates.isEmpty()) {
            rate = rates.stream().max(Comparator.comparing(v -> abs(v.getBid() - v.getAsk()))).get();
            return Optional.of(rate);
        }
        return Optional.empty();
    }

}
