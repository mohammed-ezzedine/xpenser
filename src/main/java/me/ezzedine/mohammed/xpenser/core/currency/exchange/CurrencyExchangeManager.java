package me.ezzedine.mohammed.xpenser.core.currency.exchange;

import me.ezzedine.mohammed.xpenser.core.currency.CurrencyCode;

import java.math.BigDecimal;

public interface CurrencyExchangeManager {

    BigDecimal convert(BigDecimal amount, CurrencyCode sourceCurrency, CurrencyCode targetCurrency);
}
