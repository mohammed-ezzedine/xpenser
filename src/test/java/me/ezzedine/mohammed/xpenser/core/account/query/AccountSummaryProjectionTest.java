package me.ezzedine.mohammed.xpenser.core.account.query;

import me.ezzedine.mohammed.xpenser.core.account.opening.AccountOpenedEvent;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

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
        projection.on(new AccountOpenedEvent("id", "name"));
        verify(queryUpdateEmitter).emit(eq(FetchAccountSummariesQuery.class), any(), eq(List.of(new AccountSummary("id", "name"))));
    }

    @Test
    @DisplayName("it should return the account summaries when the fetch account summaries query is issued")
    void it_should_return_the_account_summaries_when_the_fetch_account_summaries_query_is_issued() {
        projection.on(new AccountOpenedEvent("id", "name"));
        assertEquals(List.of(new AccountSummary("id", "name")), projection.handle(new FetchAccountSummariesQuery()));
    }
}