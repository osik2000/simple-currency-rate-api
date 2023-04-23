package pl.pawelosinski.dynatrace.nbp.task.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import pl.pawelosinski.dynatrace.nbp.task.model.CurrencyRateTable;
import pl.pawelosinski.dynatrace.nbp.task.model.MinMaxRate;
import pl.pawelosinski.dynatrace.nbp.task.model.Rate;
import pl.pawelosinski.dynatrace.nbp.task.service.CurrencyService;

import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RestController
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping("/averagerate/{date}")
    public ResponseEntity<CurrencyRateTable> averageExchangeRate(@RequestParam(value = "currency") String currency, @PathVariable String date) {

        try {
            return ResponseEntity.status(OK).body(currencyService.getRateFromDay(currency, date));
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == NOT_FOUND) {
                return ResponseEntity.status(NOT_FOUND).build();
            } else if (ex.getStatusCode() == BAD_REQUEST) {
                return ResponseEntity.status(BAD_REQUEST).build();
            } else {
                return ResponseEntity.status(ex.getStatusCode()).build();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/minmaxrate/{n}")
    public ResponseEntity<Optional<MinMaxRate>> averageExchangeRate(@RequestParam(value = "currency") String currency, @PathVariable int n) {
        try {
            return ResponseEntity.status(OK).body(currencyService.getMinMaxFromQuotations(n, currency));
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == NOT_FOUND) {
                return ResponseEntity.status(NOT_FOUND).build();
            } else if (ex.getStatusCode() == BAD_REQUEST) {
                return ResponseEntity.status(BAD_REQUEST).build();
            } else {
                return ResponseEntity.status(ex.getStatusCode()).build();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/majordifference/{n}")
    public ResponseEntity<Optional<Rate>> majorDifference(@RequestParam(value = "currency") String currency, @PathVariable int n) {
        try {
            return ResponseEntity.status(OK).body(currencyService.getDifferenceFromQuotations(n, currency));
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == NOT_FOUND) {
                return ResponseEntity.status(NOT_FOUND).build();
            } else if (ex.getStatusCode() == BAD_REQUEST) {
                return ResponseEntity.status(BAD_REQUEST).build();
            } else {
                return ResponseEntity.status(ex.getStatusCode()).build();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
