package me.ezzedine.mohammed.xpenser.core.account.transactions;

import lombok.Builder;
import lombok.NonNull;
import me.ezzedine.mohammed.xpenser.core.currency.CurrencyCode;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;
import java.util.Date;

@Builder
public record DepositMoneyCommand(
        @NonNull String transactionId,
        @TargetAggregateIdentifier
        @NonNull String accountId,
        @NonNull BigDecimal amount,
        CurrencyCode currencyCode,
        @NonNull String note,
        @NonNull Date timestamp
) {
}
