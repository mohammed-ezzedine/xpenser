package me.ezzedine.mohammed.xpenser.core.account.transactions;

import javax.annotation.Nonnull;
import java.util.Date;

public record MoneyWithdrewFromAccountEvent(
        @Nonnull String accountId,
        double amount,
        @Nonnull String note,
        @Nonnull Date timestamp
) { }
