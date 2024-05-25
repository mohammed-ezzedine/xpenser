package me.ezzedine.mohammed.xpenser.core.account.transactions.query;

import java.util.Date;

public record TransactionSummary(
        double amount,
        double balance,
        Date timestamp
) {
}
