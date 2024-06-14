package me.ezzedine.mohammed.xpenser.infra.currency.exchange;

import me.ezzedine.mohammed.xpenser.core.currency.CurrencyCode;

import java.util.Map;

public record OpenExchangeCurrencyRatesApiResponse(
    CurrencyCode base,
    Map<CurrencyCode, Double> rates
) { }
