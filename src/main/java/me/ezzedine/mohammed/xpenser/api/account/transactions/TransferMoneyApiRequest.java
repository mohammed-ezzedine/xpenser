package me.ezzedine.mohammed.xpenser.api.account.transactions;

import javax.annotation.Nonnull;
import java.math.BigDecimal;

public record TransferMoneyApiRequest(
        @Nonnull String destinationAccountId,
        @Nonnull BigDecimal amount
) { }
