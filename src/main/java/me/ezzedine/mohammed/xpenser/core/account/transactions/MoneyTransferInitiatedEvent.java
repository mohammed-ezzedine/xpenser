package me.ezzedine.mohammed.xpenser.core.account.transactions;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.Date;

public record MoneyTransferInitiatedEvent(
    @Nonnull String transactionId,
    @Nonnull String sourceAccountId,
    @Nonnull String destinationAccountId,
    @Nonnull BigDecimal amount,
    @Nonnull Date timestamp
) { }
