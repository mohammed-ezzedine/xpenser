package me.ezzedine.mohammed.xpenser.core.account.query;

import me.ezzedine.mohammed.xpenser.core.account.budget.Budget;

public record BudgetSummary(
    Currency currency,
    double amount
) {

    public static BudgetSummary from(Budget budget) {
        return new BudgetSummary(Currency.from(budget.getCurrency()), budget.getAmount());
    }
}
