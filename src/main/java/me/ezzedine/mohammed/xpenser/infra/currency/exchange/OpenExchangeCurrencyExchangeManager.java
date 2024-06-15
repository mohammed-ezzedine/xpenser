package me.ezzedine.mohammed.xpenser.infra.currency.exchange;

import me.ezzedine.mohammed.xpenser.core.currency.CurrencyCode;
import me.ezzedine.mohammed.xpenser.core.currency.exchange.CurrencyExchangeManager;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Service
public class OpenExchangeCurrencyExchangeManager implements CurrencyExchangeManager {

    private final OpenExchangeConfiguration configuration;
    private final RestTemplate restTemplate;

    public OpenExchangeCurrencyExchangeManager(OpenExchangeConfiguration configuration) {
        this.configuration = configuration;
        restTemplate = new RestTemplateBuilder().rootUri(configuration.getBaseUri()).build();
    }

    @Override
    public BigDecimal convert(BigDecimal amount, CurrencyCode sourceCurrency, CurrencyCode targetCurrency) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Value must be greater than zero");
        }

        if (sourceCurrency == targetCurrency) {
            return amount;
        }

        OpenExchangeCurrencyRatesApiResponse rates = fetchCurrencyRates();
        if (rates.base().equals(sourceCurrency.name())) {
            return convertFromBaseCurrency(amount, targetCurrency, rates);
        }

        if (rates.base().equals(targetCurrency.name())) {
            return convertToBaseCurrency(amount, sourceCurrency, rates);
        }

        BigDecimal amountInBaseCurrency = convertToBaseCurrency(amount, sourceCurrency, rates);
        return convertFromBaseCurrency(amountInBaseCurrency, targetCurrency, rates);
    }

    private OpenExchangeCurrencyRatesApiResponse fetchCurrencyRates() {
        return Objects.requireNonNull(restTemplate.getForObject(getUrl(), OpenExchangeCurrencyRatesApiResponse.class));
    }

    private String getUrl() {
        return "/latest.json?app_id=%s".formatted(configuration.getAppId());
    }

    private static BigDecimal convertFromBaseCurrency(BigDecimal amount, CurrencyCode targetCurrency, OpenExchangeCurrencyRatesApiResponse rates) {
        return amount.multiply(BigDecimal.valueOf(rates.rates().get(targetCurrency.name())));
    }

    private static BigDecimal convertToBaseCurrency(BigDecimal amount, CurrencyCode sourceCurrency, OpenExchangeCurrencyRatesApiResponse rates) {
        return amount.divide(BigDecimal.valueOf(rates.rates().get(sourceCurrency.name())), RoundingMode.DOWN);
    }
}
