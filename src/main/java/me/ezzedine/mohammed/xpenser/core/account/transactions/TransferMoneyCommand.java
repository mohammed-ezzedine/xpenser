package me.ezzedine.mohammed.xpenser.core.account.transactions;

import lombok.NonNull;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;
import java.util.Date;

public record TransferMoneyCommand(
        @TargetAggregateIdentifier
        @NonNull String sourceAccountId,
        @NonNull String destinationAccountId,
        @NonNull String transactionId,
        @NonNull BigDecimal amount,
        @NonNull Date timestamp
) { }
