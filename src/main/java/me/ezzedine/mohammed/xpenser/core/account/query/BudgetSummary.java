package me.ezzedine.mohammed.xpenser.core.account.query;

import me.ezzedine.mohammed.xpenser.core.account.budget.Budget;

import javax.annotation.Nonnull;
import java.math.BigDecimal;

public record BudgetSummary(
    @Nonnull Currency currency,
    @Nonnull BigDecimal amount
) {

    public static BudgetSummary from(Budget budget) {
        return new BudgetSummary(Currency.from(budget.getCurrency()), budget.getAmount());
    }
}
