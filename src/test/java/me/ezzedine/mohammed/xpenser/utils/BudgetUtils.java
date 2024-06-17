package me.ezzedine.mohammed.xpenser.utils;

import me.ezzedine.mohammed.xpenser.core.account.budget.Budget;
import me.ezzedine.mohammed.xpenser.core.account.summary.BudgetSummary;

import java.math.BigDecimal;
import java.util.Random;

public class BudgetUtils {

    public static final BigDecimal BUDGET_AMOUNT = BigDecimal.valueOf(new Random().nextDouble(10, 100));
    public static final BigDecimal ANOTHER_BUDGET_AMOUNT = BigDecimal.valueOf(new Random().nextDouble(10, 100));

    public static Budget budget() {
        return new Budget(CurrencyUtils.currencyCode(), BUDGET_AMOUNT);
    }

    public static BudgetSummary.BudgetSummaryBuilder budgetSummary() {
        return BudgetSummary.builder().currencyCode(CurrencyUtils.currencyCode()).amount(BUDGET_AMOUNT);
    }

    public static BudgetSummary.BudgetSummaryBuilder anotherBudgetSummary() {
        return BudgetSummary.builder().currencyCode(CurrencyUtils.anotherCurrencyCode()).amount(ANOTHER_BUDGET_AMOUNT);
    }
}
