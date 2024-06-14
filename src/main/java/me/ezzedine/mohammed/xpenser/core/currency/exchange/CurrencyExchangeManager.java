package me.ezzedine.mohammed.xpenser.core.currency.exchange;

import me.ezzedine.mohammed.xpenser.core.currency.CurrencyCode;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface CurrencyExchangeManager {

    Mono<BigDecimal> convert(BigDecimal amount, CurrencyCode sourceCurrency, CurrencyCode targetCurrency);
}
