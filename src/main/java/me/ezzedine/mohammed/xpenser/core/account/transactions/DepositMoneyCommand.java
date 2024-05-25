package me.ezzedine.mohammed.xpenser.core.account.transactions;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.Date;

public record DepositMoneyCommand(
        @TargetAggregateIdentifier
        String accountId,
        double amount,
        Date timestamp
) {
}
