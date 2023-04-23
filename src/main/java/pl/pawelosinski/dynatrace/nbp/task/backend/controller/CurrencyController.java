package pl.pawelosinski.dynatrace.nbp.task.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import pl.pawelosinski.dynatrace.nbp.task.backend.model.*;
import pl.pawelosinski.dynatrace.nbp.task.backend.model.rate.RateC;
import pl.pawelosinski.dynatrace.nbp.task.backend.model.table.TableA;
import pl.pawelosinski.dynatrace.nbp.task.backend.service.CurrencyService;

import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RestController
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping(value = "/exchanges/{currency}", produces="application/json")
    public ResponseEntity<TableA> averageExchangeRate(@PathVariable String currency, @RequestParam(value = "date") String date) {

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

    @GetMapping(value = "/minmaxrate/{currency}", produces="application/json")
    public ResponseEntity<Optional<MinMaxRate>> averageExchangeRate(@PathVariable String currency, @RequestParam(value = "quotations") int quotations) {
        try {
            return ResponseEntity.status(OK).body(currencyService.getMinMaxFromQuotations(quotations, currency));
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

    @GetMapping(value = "/majordifference/{currency}", produces="application/json")
    public ResponseEntity<Optional<RateC>> majorDifference(@PathVariable String currency, @RequestParam(value = "quotations") int quotations){
        try {
            return ResponseEntity.status(OK).body(currencyService.getDifferenceFromQuotations(quotations, currency));
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
