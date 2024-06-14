package me.ezzedine.mohammed.xpenser.infra.currency.exchange;

import me.ezzedine.mohammed.xpenser.core.currency.CurrencyCode;
import me.ezzedine.mohammed.xpenser.core.currency.exchange.CurrencyExchangeManager;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class OpenExchangeCurrencyExchangeManager implements CurrencyExchangeManager {

    private final OpenExchangeConfiguration configuration;
    private final WebClient webClient;

    public OpenExchangeCurrencyExchangeManager(OpenExchangeConfiguration configuration) {
        this.configuration = configuration;
        webClient = WebClient.builder().baseUrl(configuration.getBaseUri()).build();
    }

    @Override
    public Mono<BigDecimal> convert(BigDecimal amount, CurrencyCode sourceCurrency, CurrencyCode targetCurrency) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return Mono.error(new IllegalArgumentException("Value must be greater than zero"));
        }

        if (sourceCurrency == targetCurrency) {
            return Mono.just(amount);
        }

        Mono<OpenExchangeCurrencyRatesApiResponse> exchangeRates = webClient.get()
                .uri("latest.json?app_id=%s".formatted(configuration.getAppId()))
                .retrieve()
                .bodyToMono(OpenExchangeCurrencyRatesApiResponse.class);

        return exchangeRates.map(rates -> {
            if (rates.base().equals(sourceCurrency)) {
                return convertFromBaseCurrency(amount, targetCurrency, rates);
            }

            if (rates.base().equals(targetCurrency)) {
                return convertToBaseCurrency(amount, sourceCurrency, rates);
            }

            BigDecimal amountInBaseCurrency = convertToBaseCurrency(amount, sourceCurrency, rates);
            return convertFromBaseCurrency(amountInBaseCurrency, targetCurrency, rates);
        });
    }

    private static BigDecimal convertFromBaseCurrency(BigDecimal amount, CurrencyCode targetCurrency, OpenExchangeCurrencyRatesApiResponse rates) {
        return amount.multiply(BigDecimal.valueOf(rates.rates().get(targetCurrency)));
    }

    private static BigDecimal convertToBaseCurrency(BigDecimal amount, CurrencyCode sourceCurrency, OpenExchangeCurrencyRatesApiResponse rates) {
        return amount.divide(BigDecimal.valueOf(rates.rates().get(sourceCurrency)), RoundingMode.DOWN);
    }
}
