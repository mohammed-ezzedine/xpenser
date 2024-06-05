package me.ezzedine.mohammed.xpenser.core.account.transactions.query;

import lombok.NonNull;

import java.math.BigDecimal;
import java.util.Date;

public record TransactionSummary(
        @NonNull BigDecimal amount,
        @NonNull BigDecimal balance,
        @NonNull String note,
        @NonNull Date timestamp
) {
}
