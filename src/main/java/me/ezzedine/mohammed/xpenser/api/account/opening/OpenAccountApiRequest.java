package me.ezzedine.mohammed.xpenser.api.account.opening;

import javax.annotation.Nonnull;
import java.math.BigDecimal;

public record OpenAccountApiRequest(
        @Nonnull String name,
        @Nonnull String currency,
        @Nonnull BigDecimal initialAmount
) { }
