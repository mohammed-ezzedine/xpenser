package me.ezzedine.mohammed.xpenser.api.account.transactions;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Date;

public record AddMoneyToAccountApiRequest(
        @NotNull BigDecimal amount,
        @NotNull String note,
        Date timestamp
) {
}
