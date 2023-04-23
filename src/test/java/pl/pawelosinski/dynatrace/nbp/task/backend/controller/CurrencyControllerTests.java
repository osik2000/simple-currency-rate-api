package pl.pawelosinski.dynatrace.nbp.task.backend.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.pawelosinski.dynatrace.nbp.task.backend.model.MinMaxRate;
import pl.pawelosinski.dynatrace.nbp.task.backend.model.rate.RateC;
import pl.pawelosinski.dynatrace.nbp.task.backend.model.table.TableA;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class CurrencyControllerTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private final WireMockServer wireMockServer = new WireMockServer(8123);

    @BeforeAll
    void startWireMock() {
        wireMockServer.start();
    }

    @AfterAll
    void stopWireMock() {
        wireMockServer.stop();
    }


    @Test
    public void shouldGetRateFromDay() {
        // given
        String currency = "gbp";
        String date = "2012-01-02";

        configureFor("localhost", 8123);
        stubFor(get(urlEqualTo("/api/exchangerates/rates/a/gbp/2012-01-02/?format=json"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("fromDay.json")));

        // when
        ResponseEntity<TableA> result = testRestTemplate.getForEntity("/exchanges/{currency}?date={date}", TableA.class, currency, date);

        // then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().getRates().size());
        assertEquals("GBP", result.getBody().getCode());
        assertEquals(5.348, result.getBody().getRates().get(0).getMid(), 0.0001);
    }

    @Test
    public void shouldNotGetAnyResultFromDay() {
        // given
        String currency = "USD";
        String date = "2023-04-22";

        configureFor("localhost", 8123);
        stubFor(get(urlEqualTo("/api/exchangerates/rates/a/USD/2023-04-22/?format=json"))
                .willReturn(aResponse().withStatus(404)));

        // when
        ResponseEntity<TableA> result = testRestTemplate.getForEntity("/exchanges/{currency}?date={date}", TableA.class, currency, date);

        // then
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());
    }
    @Test
    public void shouldGetMinAndMaxRate() {
        // given
        String currency = "gbp";
        int quotations = 5;

        configureFor("localhost", 8123);
        stubFor(get(urlEqualTo("/api/exchangerates/rates/a/gbp/last/5/?format=json"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("minMax.json")));

        // when
        ResponseEntity<MinMaxRate> result = testRestTemplate.getForEntity("/extremum/{currency}?quotations={quotations}", MinMaxRate.class, currency, quotations);

        // then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(5.4321, result.getBody().getMax().getMid(), 0.0001);
        assertEquals(1.2345, result.getBody().getMin().getMid(), 0.0001);
    }

    @Test
    public void shouldNotGetAnyResultMinMaxWithWrongParam() {
        // given
        String currency = "gbp";
        int quotations = 305;

        configureFor("localhost", 8123);
        stubFor(get(urlEqualTo("/api/exchangerates/rates/a/gbp/last/305/?format=json"))
                .willReturn(aResponse().withStatus(400)));

        // when
        ResponseEntity<MinMaxRate> result = testRestTemplate.getForEntity("/extremum/{currency}?quotations={quotations}", MinMaxRate.class, currency, quotations);


        // then
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNull(result.getBody());
    }


    @Test
    public void shouldGetRateWithMajorDifference() {
        // given
        String currency = "gbp";
        int quotations = 5;

        configureFor("localhost", 8123);
        stubFor(get(urlEqualTo("/api/exchangerates/rates/c/gbp/last/5/?format=json"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("majorDifference.json")));

        // when
        ResponseEntity<RateC> result = testRestTemplate.getForEntity("/majordifference/{currency}?quotations={quotations}", RateC.class, currency, quotations);

        // then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("074/C/NBP/2023", result.getBody().getNo());
        assertEquals(5.4321, result.getBody().getAsk(), 0.0001);
        assertEquals(1.2345, result.getBody().getBid(), 0.0001);
    }

    @Test
    public void shouldNotGetMajorDifferenceWithWrongParam() {
        // given
        String currency = "abc";
        int quotations = 123;

        configureFor("localhost", 8123);
        stubFor(get(urlEqualTo("/api/exchangerates/rates/a/abc/last/123/?format=json"))
                .willReturn(aResponse().withStatus(404)));

        // when
        ResponseEntity<RateC> result = testRestTemplate.getForEntity("/majordifference/{currency}?quotations={quotations}", RateC.class, currency, quotations);

        // then
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());
    }
    
}