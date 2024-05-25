package me.ezzedine.mohammed.xpenser.api.account.query;

import me.ezzedine.mohammed.xpenser.api.account.ResourceUtils;
import me.ezzedine.mohammed.xpenser.core.account.query.AccountSummary;
import me.ezzedine.mohammed.xpenser.core.account.query.FetchAccountSummariesQuery;
import org.axonframework.messaging.responsetypes.ResponseType;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(AccountQueryController.class)
class AccountQueryControllerIntegrationTest {

    @Autowired
    private WebTestClient testClient;

    @MockBean
    private QueryGateway queryGateway;

    @Nested
    @DisplayName("When the user wants to fetch the list of account summaries")
    class FetchingAccountSummaries {

        @BeforeEach
        void setUp() {
            AccountSummary accountSummary = AccountSummary.builder().id("account-id").name("account-name").build();
            when(queryGateway.query(any(), any(ResponseType.class))).thenReturn(CompletableFuture.completedFuture(List.of(accountSummary)));
        }

        @Test
        @DisplayName("it should issue a fetch account summaries query")
        void it_should_issue_a_fetch_account_summaries_query() {
            testClient.get()
                    .uri("/accounts")
                    .exchange()
                    .expectStatus()
                    .is2xxSuccessful();

            verify(queryGateway).query(new FetchAccountSummariesQuery(), ResponseTypes.multipleInstancesOf(AccountSummary.class));
        }

        @Test
        @DisplayName("it should return the list of existing account summaries")
        void it_should_return_the_list_of_existing_account_summaries() {

            testClient.get()
                    .uri("/accounts")
                    .exchange()
                    .expectBody()
                    .json(ResourceUtils.resource("account/api/query/account_summaries.response.json").toString());
        }
    }
}