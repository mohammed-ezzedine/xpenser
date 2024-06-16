package me.ezzedine.mohammed.xpenser.api.account.transactions.query;

import me.ezzedine.mohammed.xpenser.api.account.ResourceUtils;
import me.ezzedine.mohammed.xpenser.core.account.AccountNotFoundException;
import me.ezzedine.mohammed.xpenser.core.account.transactions.query.FetchAccountTransactionsQuery;
import me.ezzedine.mohammed.xpenser.core.account.transactions.query.TransactionSummary;
import me.ezzedine.mohammed.xpenser.utils.AccountUtils;
import me.ezzedine.mohammed.xpenser.utils.TransactionUtils;
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

        private TransactionSummary transactionSummary;

        @BeforeEach
        void setUp() {
            transactionSummary = TransactionUtils.transactionSummary().build();
            when(queryGateway.streamingQuery(any(), any())).thenReturn(Flux.just(transactionSummary));
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
                    .json(ResourceUtils.resourceAsString("account/api/transactions/query/account_transactions.response.json")
                            .replace("\"{AMOUNT}\"", transactionSummary.amount().toString())
                            .replace("\"{BALANCE}\"", transactionSummary.balance().toString())
                            .replace("{NOTE}", transactionSummary.note())
                            .replace("{CATEGORY}", transactionSummary.category())
                            .replace("\"{TIMESTAMP}\"", String.valueOf(transactionSummary.timestamp().toInstant().toEpochMilli()))
                    );
        }

        @Test
        @DisplayName("it should return a status code 404 when the account does not exist")
        void it_should_return_a_status_code_404_when_the_account_does_not_exist() {
            when(queryGateway.streamingQuery(any(), any())).thenReturn(Flux.error(new AccountNotFoundException(AccountUtils.ACCOUNT_ID)));

            testClient.get()
                .uri("/accounts/%s/transactions".formatted(AccountUtils.ACCOUNT_ID))
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .json("\"Account '%s' does not exist.\"".formatted(AccountUtils.ACCOUNT_ID));
        }
    }

}