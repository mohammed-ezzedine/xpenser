package me.ezzedine.mohammed.xpenser.core.account.transactions;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import javax.annotation.Nonnull;
import java.util.Date;

public record DepositMoneyCommand(
        @TargetAggregateIdentifier
        @Nonnull String accountId,
        double amount,
        @Nonnull String note,
        @Nonnull Date timestamp
) {
}
