package me.ezzedine.mohammed.xpenser.api.account.transactions;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferMoneyApiRequest(
        @NotNull String destinationAccountId,
        @NotNull BigDecimal amount
) { }
