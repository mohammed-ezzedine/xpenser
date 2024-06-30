package me.ezzedine.mohammed.xpenser.api.expense;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.ezzedine.mohammed.xpenser.api.account.ResourceUtils;
import me.ezzedine.mohammed.xpenser.api.expense.category.ExpenseCategoryApiMapperImpl;
import me.ezzedine.mohammed.xpenser.api.serialization.SerializationConfiguration;
import me.ezzedine.mohammed.xpenser.api.serialization.YearMonthSerializer;
import me.ezzedine.mohammed.xpenser.core.expense.MonthlyReport;
import me.ezzedine.mohammed.xpenser.core.expense.MonthlyReportStorage;
import me.ezzedine.mohammed.xpenser.core.expense.category.ExpenseCategoryService;
import me.ezzedine.mohammed.xpenser.core.expense.category.report.ExpenseCategoryMonthlyReport;
import me.ezzedine.mohammed.xpenser.core.expense.category.report.FetchMonthlyExpensesReportByCategoriesQuery;
import me.ezzedine.mohammed.xpenser.utils.ExpenseCategoryUtils;
import me.ezzedine.mohammed.xpenser.utils.MonthlyReportUtils;
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Month;
import java.time.YearMonth;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@WebFluxTest(MonthlyReportController.class)
@ContextConfiguration(classes = {
    YearMonthSerializer.class,
    SerializationConfiguration.class,
    MonthlyReportController.class,
    MonthlyReportApiMapperImpl.class,
    ExpenseCategoryApiMapperImpl.class
})
class MonthlyReportControllerIntegrationTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private MonthlyReportStorage storage;

    @MockBean
    private ReactorQueryGateway queryGateway;

    @MockBean
    private ExpenseCategoryService expenseCategoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("it should return the correct report when asked about a specific month")
    void it_should_return_the_correct_report_when_asked_about_a_specific_month() {
        when(storage.fetch(MonthlyReportUtils.MONTH)).thenReturn(Mono.just(MonthlyReportUtils.monthlyReport().build()));
        MonthlyReport firstMonthReport = mock(MonthlyReport.class);
        MonthlyReport lastMonthReport = mock(MonthlyReport.class);
        when(storage.fetchFirstMonthReport()).thenReturn(Mono.just(firstMonthReport));
        when(storage.fetchLastMonthReport()).thenReturn(Mono.just(lastMonthReport));
        when(firstMonthReport.getMonth()).thenReturn(YearMonth.of(2024, Month.JANUARY));
        when(lastMonthReport.getMonth()).thenReturn(YearMonth.of(2024, Month.AUGUST));

        client.get()
                .uri("/expenses/reports?month=%s".formatted(MonthlyReportUtils.MONTH))
                .exchange()
                .expectBody()
                .json(ResourceUtils.resourceAsString("expenses/report/api/report_response.json")
                        .replace("{FIRST_MONTH}", YearMonth.of(2024, Month.JANUARY).toString())
                        .replace("{LAST_MONTH}", YearMonth.of(2024, Month.AUGUST).toString())
                        .replace("{REPORT_MONTH}", MonthlyReportUtils.MONTH.toString())
                        .replace("\"{REPORT_INCOMING}\"", MonthlyReportUtils.INCOMING.toString())
                        .replace("\"{REPORT_EXPENSES}\"", MonthlyReportUtils.EXPENSES.toString())
                        .replace("\"{REPORT_TARGET}\"", MonthlyReportUtils.TARGET.toString())
                );
    }

    @Test
    @DisplayName("it should return the correct report when asked for the latest one")
    void it_should_return_the_correct_report_when_asked_for_the_latest_one() {
        MonthlyReport firstMonthReport = mock(MonthlyReport.class);
        when(storage.fetchFirstMonthReport()).thenReturn(Mono.just(firstMonthReport));
        when(storage.fetchLastMonthReport()).thenReturn(Mono.just(MonthlyReportUtils.monthlyReport().build()));
        when(firstMonthReport.getMonth()).thenReturn(YearMonth.of(2024, Month.JANUARY));

        client.get()
                .uri("/expenses/reports/latest")
                .exchange()
                .expectBody()
                .json(ResourceUtils.resourceAsString("expenses/report/api/report_response.json")
                        .replace("{FIRST_MONTH}", YearMonth.of(2024, Month.JANUARY).toString())
                        .replace("{LAST_MONTH}", MonthlyReportUtils.MONTH.toString())
                        .replace("{REPORT_MONTH}", MonthlyReportUtils.MONTH.toString())
                        .replace("\"{REPORT_INCOMING}\"", MonthlyReportUtils.INCOMING.toString())
                        .replace("\"{REPORT_EXPENSES}\"", MonthlyReportUtils.EXPENSES.toString())
                        .replace("\"{REPORT_TARGET}\"", MonthlyReportUtils.TARGET.toString())
                );
    }

    @Test
    @DisplayName("it should return the correct monthly expenses report by categories")
    void it_should_return_the_correct_monthly_expenses_report_by_categories() {
        when(queryGateway.streamingQuery(new FetchMonthlyExpensesReportByCategoriesQuery(YearMonth.of(2024, 6)), ExpenseCategoryMonthlyReport.class))
                .thenReturn(Flux.just(ExpenseCategoryUtils.monthlyReport().build()));
        when(expenseCategoryService.fetch(ExpenseCategoryUtils.EXPENSE_CATEGORY_ID)).thenReturn(Mono.just(ExpenseCategoryUtils.expenseCategory().build()));

        client.get()
                .uri("/expenses/reports/by-categories?year=2024&month=6")
                .exchange()
                .expectBody()
                .json(ResourceUtils.resourceAsString("expenses/report/api/monthly_report_by_categories.json")
                        .replace("{MONTH}", "2024-06")
                        .replace("{CATEGORY_ID}", ExpenseCategoryUtils.EXPENSE_CATEGORY_ID)
                        .replace("{CATEGORY_NAME}", ExpenseCategoryUtils.EXPENSE_CATEGORY_NAME)
                        .replace("{CATEGORY_ICON}", ExpenseCategoryUtils.EXPENSE_CATEGORY_ICON)
                        .replace("\"{CATEGORY_AMOUNT}\"", ExpenseCategoryUtils.REPORT_AMOUNT.toString())
                );
    }

}