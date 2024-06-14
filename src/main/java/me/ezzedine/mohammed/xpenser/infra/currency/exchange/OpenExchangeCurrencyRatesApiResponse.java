package me.ezzedine.mohammed.xpenser.infra.currency.exchange;

import java.util.Map;

public record OpenExchangeCurrencyRatesApiResponse(
    String base,
    Map<String, Double> rates
) { }
