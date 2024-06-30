package me.ezzedine.mohammed.xpenser.core.expense.category.report;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.YearMonth;

public interface ExpenseCategoryMonthlyReportStorage {
    Mono<ExpenseCategoryMonthlyReport> fetchByCategoryAndMonth(String category, YearMonth month);
    Flux<ExpenseCategoryMonthlyReport> fetchByMonth(YearMonth month);
    Flux<ExpenseCategoryMonthlyReport> fetchByCategory(String category);

    Mono<Void> save(ExpenseCategoryMonthlyReport report);
}
