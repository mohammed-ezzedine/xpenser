package me.ezzedine.mohammed.xpenser.core.account.transactions.query;

import lombok.Builder;
import lombok.NonNull;

import java.util.List;

@Builder
public record AccountTransactionSummary(
    @NonNull String id,
    @NonNull List<TransactionSummary> transactions
) {
}
