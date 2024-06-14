package me.ezzedine.mohammed.xpenser.core.account.transactions;

import lombok.Builder;
import lombok.NonNull;
import me.ezzedine.mohammed.xpenser.core.currency.CurrencyCode;

import java.math.BigDecimal;
import java.util.Date;

@Builder
public record MoneyWithdrewFromAccountEvent(
        @NonNull String transactionId,
        @NonNull String accountId,
        @NonNull BigDecimal amount,
        @NonNull CurrencyCode currency,
        @NonNull String note,
        @NonNull Date timestamp
) { }
