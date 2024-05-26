package me.ezzedine.mohammed.xpenser.core.account.transactions;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.Date;

public record TransferMoneyCommand(
        @TargetAggregateIdentifier
        @Nonnull String sourceAccountId,
        @Nonnull String destinationAccountId,
        @Nonnull String transactionId,
        @Nonnull BigDecimal amount,
        @Nonnull Date timestamp
) { }
