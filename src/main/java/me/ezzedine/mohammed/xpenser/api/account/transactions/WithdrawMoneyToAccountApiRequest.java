package me.ezzedine.mohammed.xpenser.api.account.transactions;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Date;

public record WithdrawMoneyToAccountApiRequest(
        @NotNull BigDecimal amount,
        @NotBlank String note,
        @NotBlank String category,
        Date timestamp
) { }
