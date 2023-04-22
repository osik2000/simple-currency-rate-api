package pl.pawelosinski.dynatrace.nbp.task.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
public class CurrencyController {

    private RestTemplate restTemplate;

    @Autowired
    public CurrencyController(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    @GetMapping("/averagerate/{date}")
    public ResponseEntity<String> averageExchangeRate(@RequestParam(value = "currency") String currency, @PathVariable String date) {

        String url = "http://api.nbp.pl/api/exchangerates/rates/a/" + currency + "/" + date + "/?format=json";
        System.out.println(url);


        try {
            return restTemplate.getForEntity(url, String.class);
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == NOT_FOUND) {
                return new ResponseEntity<>("Error 404: Resource not found", NOT_FOUND);
            } else if (ex.getStatusCode() == BAD_REQUEST) {
                return new ResponseEntity<>("Error 400: Bad Request", BAD_REQUEST);
            } else {
                return new ResponseEntity<>("Error: " + ex.getStatusCode(), ex.getStatusCode());
            }
        }
    }


}
