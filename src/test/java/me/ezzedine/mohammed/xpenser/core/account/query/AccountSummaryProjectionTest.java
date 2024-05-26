package me.ezzedine.mohammed.xpenser.core.account.query;

import me.ezzedine.mohammed.xpenser.core.account.budget.Budget;
import me.ezzedine.mohammed.xpenser.core.account.budget.Currencies;
import me.ezzedine.mohammed.xpenser.core.account.budget.CurrencyCode;
import me.ezzedine.mohammed.xpenser.core.account.opening.AccountOpenedEvent;
import me.ezzedine.mohammed.xpenser.core.account.transactions.MoneyDepositedInAccountEvent;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AccountSummaryProjectionTest {

    private QueryUpdateEmitter queryUpdateEmitter;
    private AccountSummaryProjection projection;

    @BeforeEach
    void setUp() {
        queryUpdateEmitter = mock(QueryUpdateEmitter.class);
        projection = new AccountSummaryProjection(queryUpdateEmitter);
    }

    @Test
    @DisplayName("it should update the fetch account summaries query when an account opened event is issued")
    void it_should_update_the_fetch_account_summaries_query_when_an_account_opened_event_is_issued() {
        double budgetAmount = new Random().nextInt();
        projection.on(getAccountOpenedEvent(budgetAmount));

        AccountSummary accountSummary = getAccountSummary(budgetAmount);
        verify(queryUpdateEmitter).emit(eq(FetchAccountSummariesQuery.class), any(), eq(List.of(accountSummary)));
    }

    @Test
    @DisplayName("it should return the account summaries when the fetch account summaries query is issued")
    void it_should_return_the_account_summaries_when_the_fetch_account_summaries_query_is_issued() {
        double budgetAmount = new Random().nextInt();
        projection.on(getAccountOpenedEvent(budgetAmount));
        assertEquals(List.of(getAccountSummary(budgetAmount)), projection.handle(new FetchAccountSummariesQuery()));
    }

    @Test
    @DisplayName("it should update the fetch account summaries query when an account budget updated event is issued")
    void it_should_update_the_fetch_account_summaries_query_when_an_account_budget_updated_event_is_issued() {
        projection.on(getAccountOpenedEvent(5));
        projection.on(new MoneyDepositedInAccountEvent("id", 10, "", mock(Date.class)));

        AccountSummary accountSummary = getAccountSummary(15);
        verify(queryUpdateEmitter).emit(eq(FetchAccountSummariesQuery.class), any(), eq(List.of(accountSummary)));
    }

    @NotNull
    private static AccountSummary getAccountSummary(double budgetAmount) {
        Currency currency = new Currency(CurrencyCode.EURO.getValue(), Currencies.euro().symbol(), Currencies.euro().name());
        return new AccountSummary("id", "name", new BudgetSummary(currency, budgetAmount));
    }

    @NotNull
    private static AccountOpenedEvent getAccountOpenedEvent(double budgetAmount) {
        Budget budget = getBudget(budgetAmount);
        return new AccountOpenedEvent("id", "name", budget, mock(Date.class));
    }

    private static Budget getBudget(double budgetAmount) {
        return Budget.builder().amount(budgetAmount).currency(Currencies.euro()).build();
    }
}