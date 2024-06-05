package me.ezzedine.mohammed.xpenser.core.account.transactions;

import lombok.NonNull;

import java.math.BigDecimal;
import java.util.Date;

public record MoneyTransferInitiatedEvent(
    @NonNull String transactionId,
    @NonNull String sourceAccountId,
    @NonNull String destinationAccountId,
    @NonNull BigDecimal amount,
    @NonNull Date timestamp
) { }