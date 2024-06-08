package me.ezzedine.mohammed.xpenser.utils;

import me.ezzedine.mohammed.xpenser.core.account.budget.Budget;
import me.ezzedine.mohammed.xpenser.core.account.projection.summary.BudgetSummary;

import java.math.BigDecimal;
import java.util.Random;

public class BudgetUtils {

    public static final BigDecimal BUDGET_AMOUNT = BigDecimal.valueOf(new Random().nextDouble(0, 100));
    public static final BigDecimal ANOTHER_BUDGET_AMOUNT = BigDecimal.valueOf(new Random().nextDouble(0, 100));

    public static Budget budget() {
        return new Budget(CurrencyUtils.currency(), BUDGET_AMOUNT);
    }

    public static BudgetSummary.BudgetSummaryBuilder budgetSummary() {
        return BudgetSummary.builder().currencyCode(CurrencyUtils.CURRENCY_CODE).amount(BUDGET_AMOUNT);
    }
}
