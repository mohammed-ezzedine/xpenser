package me.ezzedine.mohammed.xpenser.utils;

import me.ezzedine.mohammed.xpenser.core.account.query.BudgetSummary;

import java.math.BigDecimal;
import java.util.Random;

public class BudgetUtils {

    public static final BigDecimal BUDGET_AMOUNT = BigDecimal.valueOf(new Random().nextDouble(0, 100));
    public static final BigDecimal ANOTHER_BUDGET_AMOUNT = BigDecimal.valueOf(new Random().nextDouble(0, 100));

    public static BudgetSummary budgetSummary() {
        return new BudgetSummary(CurrencyUtils.CURRENCY_CODE, BUDGET_AMOUNT);
    }

    public static BudgetSummary anotherBudgetSummary() {
        return new BudgetSummary(CurrencyUtils.ANOTHER_CURRENCY_CODE, ANOTHER_BUDGET_AMOUNT);
    }
}
