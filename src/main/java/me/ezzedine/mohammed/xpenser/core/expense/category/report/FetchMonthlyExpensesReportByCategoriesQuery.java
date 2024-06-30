package me.ezzedine.mohammed.xpenser.core.expense.category.report;

import lombok.Builder;

import java.time.YearMonth;

@Builder
public record FetchMonthlyExpensesReportByCategoriesQuery(YearMonth month) {
}
