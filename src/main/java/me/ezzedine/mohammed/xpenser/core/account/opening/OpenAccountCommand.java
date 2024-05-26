package me.ezzedine.mohammed.xpenser.core.account.opening;

import lombok.Builder;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.Date;

@Builder
public record OpenAccountCommand(
    @Nonnull String id,
    @Nonnull String name,
    @Nonnull String currencyCode,
    @Nonnull BigDecimal budgetInitialAmount,
    @Nonnull Date timestamp
) { }
