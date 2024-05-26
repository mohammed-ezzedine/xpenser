package me.ezzedine.mohammed.xpenser.core.account.transactions;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.Date;

public record WithdrawMoneyCommand(
        @TargetAggregateIdentifier
        @Nonnull String accountId,
        @Nonnull BigDecimal amount,
        @Nonnull String note,
        @Nonnull Date timestamp
) {
}
