package me.ezzedine.mohammed.xpenser.core.expense.category.report;

import me.ezzedine.mohammed.xpenser.core.expense.YearMonthFactory;
import me.ezzedine.mohammed.xpenser.utils.ExpenseCategoryUtils;
import me.ezzedine.mohammed.xpenser.utils.TransactionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.Mockito.*;

class MonthlyCategoryExpensesProjectionTest {

    private ExpenseCategoryMonthlyReportStorage storage;
    private MonthlyCategoryExpensesProjection projection;
    private YearMonthFactory yearMonthFactory;

    @BeforeEach
    void setUp() {
        storage = mock(ExpenseCategoryMonthlyReportStorage.class);
        yearMonthFactory = mock(YearMonthFactory.class);
        projection = new MonthlyCategoryExpensesProjection(storage, yearMonthFactory);

        when(yearMonthFactory.from(TransactionUtils.TRANSACTION_DATE)).thenReturn(ExpenseCategoryUtils.REPORT_MONTH);
        when(storage.save(any())).thenReturn(Mono.empty());
    }

    @Test
    @DisplayName("it should add the withdrawn money amount to the monthly report corresponding to the event timestamp")
    void it_should_add_the_withdrawn_money_amount_to_the_monthly_report_corresponding_to_the_event_timestamp() {
        when(storage.fetchByCategoryAndMonth(ExpenseCategoryUtils.EXPENSE_CATEGORY_ID, ExpenseCategoryUtils.REPORT_MONTH))
                .thenReturn(Mono.just(ExpenseCategoryUtils.monthlyReport().build()));

        projection.handle(TransactionUtils.moneyWithdrewFromAccountEvent().build(), "RegularAccountAggregate");

        ExpenseCategoryMonthlyReport expectedReport = ExpenseCategoryUtils.monthlyReport()
                .amount(ExpenseCategoryUtils.REPORT_AMOUNT.add(TransactionUtils.TRANSACTION_AMOUNT)).build();
        verify(storage).save(expectedReport);
    }

    @Test
    @DisplayName("it should create a new report when no corresponding one already exists")
    void it_should_create_a_new_report_when_no_corresponding_one_already_exists() {
        when(storage.fetchByCategoryAndMonth(ExpenseCategoryUtils.EXPENSE_CATEGORY_ID, ExpenseCategoryUtils.REPORT_MONTH))
                .thenReturn(Mono.empty());

        projection.handle(TransactionUtils.moneyWithdrewFromAccountEvent().build(), "RegularAccountAggregate");

        ExpenseCategoryMonthlyReport expectedReport = ExpenseCategoryUtils.monthlyReport()
                .amount(TransactionUtils.TRANSACTION_AMOUNT).build();
        verify(storage).save(expectedReport);
    }

    @Test
    @DisplayName("it should ignore events with null categories")
    void it_should_ignore_events_with_null_categories() {
        projection.handle(TransactionUtils.moneyWithdrewFromAccountEvent().category(null).build(), "RegularAccountAggregate");

        verifyNoInteractions(storage);
    }

    @Test
    @DisplayName("it should ignore events corresponding to accounts not of type regular account")
    void it_should_ignore_events_corresponding_to_accounts_not_of_type_regular_account() {
        projection.handle(TransactionUtils.moneyWithdrewFromAccountEvent().build(), UUID.randomUUID().toString());

        verifyNoInteractions(storage);
    }

    @Test
    @DisplayName("it should fetch the reports from the storage when handling a query to fetch the reports of the month by categories")
    void it_should_fetch_the_reports_from_the_storage_when_handling_a_query_to_fetch_the_reports_of_the_month_by_categories() {
        when(storage.fetchByMonth(ExpenseCategoryUtils.REPORT_MONTH)).thenReturn(Flux.just(ExpenseCategoryUtils.monthlyReport().build()));

        Flux<ExpenseCategoryMonthlyReport> flux = projection.handle(new FetchMonthlyExpensesReportByCategoriesQuery(ExpenseCategoryUtils.REPORT_MONTH));

        StepVerifier.create(flux)
                .expectNext(ExpenseCategoryUtils.monthlyReport().build())
                .verifyComplete();
    }

    @Test
    @DisplayName("it should fetch the reports from the storage when handling a query to fetch the reports of a category across months")
    void it_should_fetch_the_reports_from_the_storage_when_handling_a_query_to_fetch_the_reports_of_a_category_across_months() {
        when(storage.fetchByCategory(ExpenseCategoryUtils.EXPENSE_CATEGORY_ID)).thenReturn(Flux.just(ExpenseCategoryUtils.monthlyReport().build()));

        Flux<ExpenseCategoryMonthlyReport> flux = projection.handle(new FetchMonthlyCategoryExpensesReportByQuery(ExpenseCategoryUtils.EXPENSE_CATEGORY_ID));

        StepVerifier.create(flux)
                .expectNext(ExpenseCategoryUtils.monthlyReport().build())
                .verifyComplete();
    }
}