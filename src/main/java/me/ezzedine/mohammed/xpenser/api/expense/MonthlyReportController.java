package me.ezzedine.mohammed.xpenser.api.expense;

import lombok.RequiredArgsConstructor;
import me.ezzedine.mohammed.xpenser.api.expense.category.ExpenseCategoryApiMapper;
import me.ezzedine.mohammed.xpenser.core.expense.MonthlyReportStorage;
import me.ezzedine.mohammed.xpenser.core.expense.category.ExpenseCategoryService;
import me.ezzedine.mohammed.xpenser.core.expense.category.report.ExpenseCategoryMonthlyReport;
import me.ezzedine.mohammed.xpenser.core.expense.category.report.FetchMonthlyCategoryExpensesReportByQuery;
import me.ezzedine.mohammed.xpenser.core.expense.category.report.FetchMonthlyExpensesReportByCategoriesQuery;
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.YearMonth;
import java.util.Comparator;

@RestController
@RequestMapping("expenses/reports")
@RequiredArgsConstructor
public class MonthlyReportController {

    private final MonthlyReportStorage storage;
    private final MonthlyReportApiMapper mapper;
    private final ReactorQueryGateway queryGateway;
    private final ExpenseCategoryApiMapper categoryApiMapper;
    private final ExpenseCategoryService expenseCategoryService;

    @GetMapping
    public Mono<MonthlyReportApiResponse> fetchMonthlyReport(@RequestParam YearMonth month) {
        return storage.fetch(month)
                .zipWith(storage.fetchFirstMonthReport())
                .zipWith(storage.fetchLastMonthReport())
                .map(result -> MonthlyReportApiResponse.builder()
                        .report(mapper.map(result.getT1().getT1()))
                        .first(result.getT1().getT2().getMonth().toString())
                        .last(result.getT2().getMonth().toString())
                        .build());
    }

    @GetMapping("latest")
    public Mono<MonthlyReportApiResponse> fetchLatestMonthlyReport() {
        return storage.fetchLastMonthReport()
                .zipWith(storage.fetchFirstMonthReport())
                .map(result -> MonthlyReportApiResponse.builder()
                        .report(mapper.map(result.getT1()))
                        .first(result.getT2().getMonth().toString())
                        .last(result.getT1().getMonth().toString())
                        .build());
    }

    @GetMapping("by-categories")
    public Mono<MonthlyCategoriesReportApiResponse> fetchMonthlyCategoriesExpenses(@RequestParam int year, @RequestParam int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        FetchMonthlyExpensesReportByCategoriesQuery query = FetchMonthlyExpensesReportByCategoriesQuery.builder().month(yearMonth).build();
        return queryGateway.streamingQuery(query, ExpenseCategoryMonthlyReport.class)
                .flatMap(categoryMonthlyReport -> expenseCategoryService.fetch(categoryMonthlyReport.category())
                        .map(categoryApiMapper::map)
                        .zipWith(Mono.just(categoryMonthlyReport.amount())))
                .map(categoryAmountTuple -> MonthlyCategoriesReportApiResponse.CategoryExpenseReport.builder().category(categoryAmountTuple.getT1()).amount(categoryAmountTuple.getT2()).build())
                .collectList()
                .map(categoryReports -> MonthlyCategoriesReportApiResponse.builder().month(yearMonth).categories(categoryReports.stream().sorted(Comparator.comparing(category -> category.category().name())).toList()).build());
    }

    @GetMapping("categories/{categoryId}")
    public Flux<ExpenseCategoryMonthlyReport> fetchCategoryMonthlyExpenses(@PathVariable String categoryId) {
        return queryGateway.streamingQuery(new FetchMonthlyCategoryExpensesReportByQuery(categoryId), ExpenseCategoryMonthlyReport.class);
    }
}
