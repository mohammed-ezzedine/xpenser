package me.ezzedine.mohammed.xpenser.core.account.transactions.query;

import javax.annotation.Nonnull;
import java.util.Date;

public record TransactionSummary(
        double amount,
        double balance,
        @Nonnull String note,
        @Nonnull Date timestamp
) {
}
