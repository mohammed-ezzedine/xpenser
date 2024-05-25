package me.ezzedine.mohammed.xpenser.core.account.transactions;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record DepositMoneyCommand(
        @TargetAggregateIdentifier
        String accountId,
        double amount
) {
}
