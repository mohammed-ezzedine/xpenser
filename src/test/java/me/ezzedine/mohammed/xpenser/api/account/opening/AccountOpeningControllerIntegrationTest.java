package me.ezzedine.mohammed.xpenser.api.account.opening;

import me.ezzedine.mohammed.xpenser.api.account.ResourceUtils;
import me.ezzedine.mohammed.xpenser.core.account.opening.AccountIdGenerator;
import me.ezzedine.mohammed.xpenser.core.account.opening.OpenAccountCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(AccountOpeningController.class)
class AccountOpeningControllerIntegrationTest {

    private static final String ACCOUNT_ID = "account-id";

    @Autowired
    private WebTestClient client;

    @MockBean
    private AccountIdGenerator idGenerator;

    @MockBean
    private CommandGateway commandGateway;

    @BeforeEach
    void setUp() {
        when(commandGateway.send(any())).thenReturn(CompletableFuture.completedFuture(new Object()));
        when(idGenerator.generate()).thenReturn(Mono.just(ACCOUNT_ID));
    }

    @Nested
    @DisplayName("When the user requests to open a new account")
    class OpeningNewAccount {

        @Test
        @DisplayName("it should issue an open account command")
        void it_should_issue_an_open_account_command() {
            client.post()
                    .uri("/accounts/open")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(ResourceUtils.resource("account/api/opening/open_account.request.json"))
                    .exchange()
                    .expectStatus()
                    .is2xxSuccessful();

            verify(commandGateway).send(OpenAccountCommand.builder().name("account-name").id(ACCOUNT_ID).currencyCode("currency-code").budgetInitialAmount(81.0).build());
        }

        @Test
        @DisplayName("it should return the account id")
        void it_should_return_the_account_id() {
            client.post()
                    .uri("/accounts/open")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(ResourceUtils.resource("account/api/opening/open_account.request.json"))
                    .exchange()
                    .expectBody()
                    .json(ResourceUtils.resource("account/api/opening/open_account.response.json").toString());
        }
    }

}