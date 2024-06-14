package me.ezzedine.mohammed.xpenser.infra.currency.exchange;

import me.ezzedine.mohammed.xpenser.api.account.ResourceUtils;
import me.ezzedine.mohammed.xpenser.core.currency.CurrencyCode;
import me.ezzedine.mohammed.xpenser.utils.CurrencyUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.MediaType;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

class OpenExchangeCurrencyExchangeManagerTest {

    public static final String APP_ID = "app-id";
    private OpenExchangeCurrencyExchangeManager currencyExchangeManager;
    private ClientAndServer clientAndServer;

    @BeforeEach
    void setUp() {
        clientAndServer = startClientAndServer(8080);

        clientAndServer
                .when(request()
                        .withMethod("GET")
                        .withPath("/latest.json")
                        .withQueryStringParameter("app_id", APP_ID))
                .respond(response()
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody(ResourceUtils.resourceAsString("currency/core/open_exchange_currencies_rates.json")));

        OpenExchangeConfiguration configuration = new OpenExchangeConfiguration();
        configuration.setAppId(APP_ID);
        configuration.setBaseUri("http://localhost:8080/");
        currencyExchangeManager = new OpenExchangeCurrencyExchangeManager(configuration);
    }

    @AfterEach
    void tearDown() {
        clientAndServer.stop();
    }

    @Test
    @DisplayName("it should fail if the specified amount is negative")
    void it_should_fail_if_the_specified_amount_is_negative() {
        Mono<BigDecimal> result = currencyExchangeManager.convert(BigDecimal.valueOf(-1), CurrencyUtils.currencyCode(), CurrencyUtils.anotherCurrencyCode());
        StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    @DisplayName("it should fail if the specified amount is zero")
    void it_should_fail_if_the_specified_amount_is_zero() {
        Mono<BigDecimal> result = currencyExchangeManager.convert(BigDecimal.ZERO, CurrencyUtils.currencyCode(), CurrencyUtils.anotherCurrencyCode());
        StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    @DisplayName("it should return the same amount when the source and target currencies are the same")
    void it_should_return_the_same_amount_when_the_source_and_target_currencies_are_the_same() {
        BigDecimal amount = BigDecimal.valueOf(new Random().nextDouble(10, 100));
        Mono<BigDecimal> result = currencyExchangeManager.convert(amount, CurrencyUtils.currencyCode(), CurrencyUtils.currencyCode());
        StepVerifier.create(result)
                .expectNext(amount)
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("it should convert the amount correctly when the source currency is usd")
    void it_should_convert_the_amount_correctly_when_the_source_currency_is_usd() {
        BigDecimal amount = BigDecimal.valueOf(new Random().nextDouble(10, 100));
        Mono<BigDecimal> result = currencyExchangeManager.convert(amount, CurrencyCode.USD, CurrencyCode.LBP);
        StepVerifier.create(result)
                .expectNext(amount.multiply(BigDecimal.valueOf(89600.0)))
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("it should convert the amount correctly when the target currency is usd")
    void it_should_convert_the_amount_correctly_when_the_target_currency_is_usd() {
        BigDecimal amount = BigDecimal.valueOf(new Random().nextDouble(10, 100));
        Mono<BigDecimal> result = currencyExchangeManager.convert(amount, CurrencyCode.EUR, CurrencyCode.USD);
        StepVerifier.create(result)
                .expectNext(amount.divide(BigDecimal.valueOf(0.934739), RoundingMode.DOWN))
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("it should convert the amount correctly when neither the source nor the target currency is usd")
    void it_should_convert_the_amount_correctly_when_neither_the_source_nor_the_target_currency_is_usd() {
        BigDecimal amount = BigDecimal.valueOf(new Random().nextDouble(10, 100));
        Mono<BigDecimal> result = currencyExchangeManager.convert(amount, CurrencyCode.CHF, CurrencyCode.EUR);
        StepVerifier.create(result)
                .expectNext(amount
                        .divide(BigDecimal.valueOf(0.890161), RoundingMode.DOWN)
                        .multiply(BigDecimal.valueOf(0.934739)))
                .expectComplete()
                .verify();
    }
}