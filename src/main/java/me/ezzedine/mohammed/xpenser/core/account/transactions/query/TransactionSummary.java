package me.ezzedine.mohammed.xpenser.core.account.transactions.query;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.Date;

public record TransactionSummary(
        @Nonnull BigDecimal amount,
        @Nonnull BigDecimal balance,
        @Nonnull String note,
        @Nonnull Date timestamp
) {
}
