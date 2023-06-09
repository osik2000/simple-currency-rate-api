package pl.pawelosinski.dynatrace.nbp.task.backend.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import pl.pawelosinski.dynatrace.nbp.task.backend.model.MinMaxRate;
import pl.pawelosinski.dynatrace.nbp.task.backend.model.rate.RateC;
import pl.pawelosinski.dynatrace.nbp.task.backend.model.table.TableA;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class CurrencyServiceTests {

    @Autowired
    private CurrencyService currencyService;

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
    public void shouldGetRateFromDay() throws Exception {
        // given
        String currency = "gbp";
        String date = "2012-01-02";

        configureFor("localhost", 8123);
        stubFor(get(urlEqualTo("/api/exchangerates/rates/a/gbp/2012-01-02/?format=json"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("fromDay.json")));

        // when
        TableA result = currencyService.getRateFromDay(currency, date).isPresent()?
                currencyService.getRateFromDay(currency, date).get() : null;

        // then
        assertNotNull(result);
        assertEquals(1, result.getRates().size());
        assertEquals("GBP", result.getCode());
        assertEquals(5.348, result.getRates().get(0).getMid(), 0.0001);
    }

    @Test
    public void shouldNotGetAnyResultFromDay() {
        // given
        String currency = "USD";
        String date = "2023-04-22";

        configureFor("localhost", 8123);
        stubFor(get(urlEqualTo("/api/exchangerates/rates/a/USD/2023-04-22/?format=json"))
                .willReturn(aResponse().withStatus(404)));


        // then
        assertThrows(HttpClientErrorException.class, () -> currencyService.getRateFromDay(currency, date));
    }
    @Test
    public void shouldGetMinAndMaxRate() throws Exception {
        // given
        String currency = "gbp";
        int quotations = 5;

        configureFor("localhost", 8123);
        stubFor(get(urlEqualTo("/api/exchangerates/rates/a/gbp/last/5/?format=json"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("minMax.json")));

        // when
        MinMaxRate result = currencyService.getMinMaxFromQuotations(quotations, currency).isPresent()?
                currencyService.getMinMaxFromQuotations(quotations, currency).get() : null;


        // then
        assertNotNull(result);
        assertEquals(5.4321, result.getMax().getMid(), 0.0001);
        assertEquals(1.2345, result.getMin().getMid(), 0.0001);
    }

    @Test
    public void shouldNotGetAnyResultMinMaxWithWrongParam() {
        // given
        String currency = "gbp";
        int quotations = 305;

        configureFor("localhost", 8123);
        stubFor(get(urlEqualTo("/api/exchangerates/rates/a/gbp/last/305/?format=json"))
                .willReturn(aResponse().withStatus(400)));

        // then
        assertThrows(HttpClientErrorException.class, () -> currencyService.getMinMaxFromQuotations(quotations, currency));
    }


    @Test
    public void shouldGetRateWithMajorDifference() throws Exception {
        // given
        String currency = "gbp";
        int quotations = 5;

        configureFor("localhost", 8123);
        stubFor(get(urlEqualTo("/api/exchangerates/rates/c/gbp/last/5/?format=json"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("majorDifference.json")));

        // when
        RateC result = currencyService.getDifferenceFromQuotations(quotations, currency).isPresent()?
                currencyService.getDifferenceFromQuotations(quotations, currency).get() : null;


        // then
        assertNotNull(result);
        assertEquals("074/C/NBP/2023", result.getNo());
        assertEquals(5.4321, result.getAsk(), 0.0001);
        assertEquals(1.2345, result.getBid(), 0.0001);
    }

    @Test
    public void shouldNotGetMajorDifferenceWithWrongParam() {
        // given
        String currency = "abc";
        int quotations = 123;

        configureFor("localhost", 8123);
        stubFor(get(urlEqualTo("/api/exchangerates/rates/a/abc/last/123/?format=json"))
                .willReturn(aResponse().withStatus(404)));

        // then
        assertThrows(HttpClientErrorException.class, () -> currencyService.getMinMaxFromQuotations(quotations, currency));
    }

}