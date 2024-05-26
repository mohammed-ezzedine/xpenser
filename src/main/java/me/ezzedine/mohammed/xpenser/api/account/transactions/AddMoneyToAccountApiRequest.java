package me.ezzedine.mohammed.xpenser.api.account.transactions;

import javax.annotation.Nonnull;
import java.math.BigDecimal;

public record AddMoneyToAccountApiRequest(
        @Nonnull BigDecimal amount,
        @Nonnull String note
) { }
