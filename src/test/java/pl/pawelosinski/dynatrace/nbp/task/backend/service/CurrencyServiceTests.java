package pl.pawelosinski.dynatrace.nbp.task.backend.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import pl.pawelosinski.dynatrace.nbp.task.backend.model.table.Table;
import pl.pawelosinski.dynatrace.nbp.task.backend.model.table.TableA;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("integration")
public class CurrencyServiceTests {

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private TestRestTemplate restTemplate;

    private final WireMockServer wireMockServer = new WireMockServer(8123);

    @BeforeAll
    void startWireMock() {
        wireMockServer.start();
    }


    @AfterEach
    protected void clearAfterEach() {
        wireMockServer.resetMappings();
        wireMockServer.resetAll();
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
        TableA result = currencyService.getRateFromDay(currency, date);

        System.out.println(result.getCurrency());

        // then
        assertEquals(1, result.getRates().size());
        assertEquals("GBP", result.getCode());
        assertEquals(5.348, result.getRates().get(0).getMid(), 0.0001);
    }

    @Test
    public void shouldNotGetAnyResultFromDay() {
        // Given
        String currency = "USD";
        String date = "2023-04-22";
        stubFor(get(urlEqualTo("/api/exchangerates/rates/a/USD/2023-04-22/?format=json"))
                .willReturn(aResponse().withStatus(404)));


        // then
        assertThrows(HttpClientErrorException.class, () -> {
            currencyService.getRateFromDay(currency, date);
        });
    }


}