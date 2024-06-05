package me.ezzedine.mohammed.xpenser.core.account.query;

import lombok.NonNull;
import me.ezzedine.mohammed.xpenser.core.account.budget.Budget;

import java.math.BigDecimal;

public record BudgetSummary(
    @NonNull Currency currency,
    @NonNull BigDecimal amount
) {

    public static BudgetSummary from(Budget budget) {
        return new BudgetSummary(Currency.from(budget.getCurrency()), budget.getAmount());
    }
}
