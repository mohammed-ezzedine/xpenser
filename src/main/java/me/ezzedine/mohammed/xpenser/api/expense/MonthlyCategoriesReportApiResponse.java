package me.ezzedine.mohammed.xpenser.api.expense;

import lombok.Builder;
import me.ezzedine.mohammed.xpenser.api.expense.category.ExpenseCategoryApiModel;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;


@Builder
public record MonthlyCategoriesReportApiResponse (
    YearMonth month,
    List<CategoryExpenseReport> categories
) {

    @Builder
    record CategoryExpenseReport(ExpenseCategoryApiModel category, BigDecimal amount) { }
}
