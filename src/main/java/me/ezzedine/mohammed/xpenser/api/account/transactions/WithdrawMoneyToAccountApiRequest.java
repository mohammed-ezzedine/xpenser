package me.ezzedine.mohammed.xpenser.api.account.transactions;

import javax.annotation.Nonnull;
import java.math.BigDecimal;

public record WithdrawMoneyToAccountApiRequest(
        @Nonnull BigDecimal amount,
        @Nonnull String note
) { }
