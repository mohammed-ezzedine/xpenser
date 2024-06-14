package me.ezzedine.mohammed.xpenser.core.account.transactions;

import lombok.Builder;
import lombok.NonNull;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;
import java.util.Date;

@Builder
public record WithdrawMoneyCommand(
        @NonNull String transactionId,
        @TargetAggregateIdentifier
        @NonNull String accountId,
        @NonNull BigDecimal amount,
        @NonNull String note,
        @NonNull Date timestamp
) {
}
