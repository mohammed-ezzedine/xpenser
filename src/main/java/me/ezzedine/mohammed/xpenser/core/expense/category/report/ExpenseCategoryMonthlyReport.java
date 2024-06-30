package me.ezzedine.mohammed.xpenser.core.expense.category.report;

import lombok.Builder;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.YearMonth;

@Builder(toBuilder = true)
public record ExpenseCategoryMonthlyReport(
    @NonNull String category,
    @NonNull BigDecimal amount,
    @NonNull YearMonth month
) {
}
