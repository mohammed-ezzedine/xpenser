package me.ezzedine.mohammed.xpenser.core.account.query;

import lombok.Builder;
import lombok.NonNull;
import me.ezzedine.mohammed.xpenser.core.account.budget.Budget;
import me.ezzedine.mohammed.xpenser.core.account.budget.CurrencyCode;

import java.math.BigDecimal;

@Builder
public record BudgetSummary(
    @NonNull CurrencyCode currencyCode,
    @NonNull BigDecimal amount
) {

    public static BudgetSummary from(Budget budget) {
        return new BudgetSummary(budget.getCurrency().code(), budget.getAmount());
    }
}
