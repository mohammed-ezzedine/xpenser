package me.ezzedine.mohammed.xpenser.core.account.opening;

import lombok.Builder;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.Date;

@Builder
public record OpenAccountCommand(
    @NonNull String id,
    @NonNull String name,
    @NonNull String currencyCode,
    @NonNull BigDecimal budgetInitialAmount,
    @NonNull Date timestamp
) { }
