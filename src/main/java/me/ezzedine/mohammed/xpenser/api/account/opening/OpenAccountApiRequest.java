package me.ezzedine.mohammed.xpenser.api.account.opening;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OpenAccountApiRequest(
        @NotNull String name,
        @NotNull String currency,
        @NotNull BigDecimal initialAmount
) { }
