package me.ezzedine.mohammed.xpenser.api.account.query;

import me.ezzedine.mohammed.xpenser.api.account.ResourceUtils;
import me.ezzedine.mohammed.xpenser.core.account.AccountNotFoundException;
import me.ezzedine.mohammed.xpenser.core.account.summary.AccountSummary;
import me.ezzedine.mohammed.xpenser.core.account.summary.BudgetSummary;
import me.ezzedine.mohammed.xpenser.core.account.summary.FetchAccountSummariesQuery;
import me.ezzedine.mohammed.xpenser.core.account.summary.FetchAccountSummaryQuery;
import me.ezzedine.mohammed.xpenser.core.currency.CurrencyCode;
import me.ezzedine.mohammed.xpenser.utils.AccountUtils;
import me.ezzedine.mohammed.xpenser.utils.BudgetUtils;
import me.ezzedine.mohammed.xpenser.utils.CurrencyUtils;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(AccountQueryController.class)
class AccountQueryControllerIntegrationTest {

    @Autowired
    private WebTestClient testClient;

    @MockBean
    private ReactorQueryGateway queryGateway;

    @Nested
    @DisplayName("When the user wants to fetch the list of account summaries")
    class FetchingAccountSummaries {

        @BeforeEach
        void setUp() {
            AccountSummary accountSummary = new AccountSummary("account-id", "account-name", new BudgetSummary(CurrencyCode.EUR, BigDecimal.valueOf(14.5)));
            when(queryGateway.streamingQuery(any(), any())).thenReturn(Flux.just(accountSummary));
        }

        @Test
        @DisplayName("it should issue a fetch account summaries query")
        void it_should_issue_a_fetch_account_summaries_query() {
            testClient.get()
                    .uri("/accounts")
                    .exchange()
                    .expectStatus()
                    .is2xxSuccessful();

            verify(queryGateway).streamingQuery(new FetchAccountSummariesQuery(), AccountSummary.class);
        }

        @Test
        @DisplayName("it should return the list of existing account summaries")
        void it_should_return_the_list_of_existing_account_summaries() {

            testClient.get()
                    .uri("/accounts")
                    .exchange()
                    .expectBody()
                    .json(ResourceUtils.resourceAsString("account/api/query/account_summaries.response.json"));
        }
    }

    @Nested
    @DisplayName("When fetching the details of an account given its ID")
    class FetchingAccountDetails {

        @BeforeEach
        void setUp() {
            when(queryGateway.streamingQuery(any(), any())).thenReturn(Flux.just(AccountUtils.accountSummary().build()));
        }

        @Test
        @DisplayName("it should issue a fetch account query")
        void it_should_issue_a_fetch_account_query() {
            testClient.get()
                    .uri("/accounts/" + AccountUtils.ACCOUNT_ID)
                    .exchange()
                    .expectStatus()
                    .is2xxSuccessful();

            verify(queryGateway).streamingQuery(new FetchAccountSummaryQuery(AccountUtils.ACCOUNT_ID), AccountSummary.class);
        }

        @Test
        @DisplayName("it should return the summary of account")
        void it_should_return_the_summary_of_account() {
            testClient.get()
                    .uri("/accounts/" + AccountUtils.ACCOUNT_ID)
                    .exchange()
                    .expectBody()
                    .json(ResourceUtils.resourceAsString("account/api/query/account_summary.response.json")
                            .replace("{ACCOUNT_ID}", AccountUtils.ACCOUNT_ID)
                            .replace("{ACCOUNT_NAME}", AccountUtils.ACCOUNT_NAME)
                            .replace("{CURRENCY_CODE}", CurrencyUtils.currencyCode().name())
                            .replace("\"{BUDGET_AMOUNT}\"", BudgetUtils.BUDGET_AMOUNT.toString())
                    );
        }

        @Test
        @DisplayName("it should return 404 when the account does not exist")
        void it_should_return_404_when_the_account_does_not_exist() {
            when(queryGateway.streamingQuery(any(), any())).thenReturn(Flux.error(new AccountNotFoundException(AccountUtils.ACCOUNT_ID)));

            testClient.get()
                    .uri("/accounts/" + AccountUtils.ACCOUNT_ID)
                    .exchange()
                    .expectStatus()
                    .isNotFound()
                    .expectBody()
                    .json("\"Account '%s' does not exist.\"".formatted(AccountUtils.ACCOUNT_ID));
        }
    }
}