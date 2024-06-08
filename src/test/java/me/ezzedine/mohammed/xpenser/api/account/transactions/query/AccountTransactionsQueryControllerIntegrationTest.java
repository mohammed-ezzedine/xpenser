package me.ezzedine.mohammed.xpenser.api.account.transactions.query;

import me.ezzedine.mohammed.xpenser.api.account.ResourceUtils;
import me.ezzedine.mohammed.xpenser.core.account.transactions.query.FetchAccountTransactionsQuery;
import me.ezzedine.mohammed.xpenser.core.account.transactions.query.TransactionSummary;
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(AccountTransactionsQueryController.class)
class AccountTransactionsQueryControllerIntegrationTest {

    @Autowired
    private WebTestClient testClient;

    @MockBean
    private ReactorQueryGateway queryGateway;

    @Nested
    @DisplayName("When fetching the transactions of an account")
    class FetchingAccountTransactions {

        @BeforeEach
        void setUp() {
            when(queryGateway.streamingQuery(any(), any())).thenReturn(Flux.just(
                    new TransactionSummary(BigDecimal.valueOf(14.3), BigDecimal.valueOf(20.3), "transaction-summary", Date.from(Instant.parse("2024-05-25T16:04:47.073Z")))));
        }

        @Test
        @DisplayName("it should issue a fetch account transactions query")
        void it_should_issue_a_fetch_account_transactions_query() {
            testClient.get()
                    .uri("/accounts/id/transactions")
                    .exchange();

            verify(queryGateway).streamingQuery(new FetchAccountTransactionsQuery("id"), TransactionSummary.class);
        }

        @Test
        @DisplayName("it should return the list of account transactions")
        void it_should_return_the_list_of_account_transactions() {
            testClient.get()
                    .uri("/accounts/id/transactions")
                    .exchange()
                    .expectStatus()
                    .is2xxSuccessful()
                    .expectBody()
                    .json(ResourceUtils.resourceAsString("account/api/transactions/query/account_transactions.response.json"));
        }
    }

}