package me.ezzedine.mohammed.xpenser.api.account.opening;

import jakarta.validation.constraints.NotNull;
import me.ezzedine.mohammed.xpenser.core.currency.CurrencyCode;

import java.math.BigDecimal;

public record OpenAccountApiRequest(
        @NotNull String name,
        @NotNull CurrencyCode currency,
        @NotNull BigDecimal initialAmount
) { }
