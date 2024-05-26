package me.ezzedine.mohammed.xpenser.core.account.transactions;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.Date;

public record MoneyWithdrewFromAccountEvent(
        @Nonnull String accountId,
        @Nonnull BigDecimal amount,
        @Nonnull String note,
        @Nonnull Date timestamp
) { }
