package me.ezzedine.mohammed.xpenser.api.account.opening;

import me.ezzedine.mohammed.xpenser.api.account.ResourceUtils;
import me.ezzedine.mohammed.xpenser.core.account.opening.AccountIdGenerator;
import me.ezzedine.mohammed.xpenser.core.account.opening.OpenAccountCommand;
import me.ezzedine.mohammed.xpenser.core.account.transactions.DateFactory;
import me.ezzedine.mohammed.xpenser.core.currency.CurrencyCode;
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

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebFluxTest(AccountOpeningController.class)
class AccountOpeningControllerIntegrationTest {

    private static final String ACCOUNT_ID = "account-id";

    @Autowired
    private WebTestClient client;

    @MockBean
    private AccountIdGenerator idGenerator;

    @MockBean
    private DateFactory dateFactory;

    @MockBean
    private CommandGateway commandGateway;

    private Date currentDate;

    @BeforeEach
    void setUp() {
        when(commandGateway.sendAndWait(any())).thenReturn(CompletableFuture.completedFuture(new Object()));
        when(idGenerator.generate()).thenReturn(Mono.just(ACCOUNT_ID));
        currentDate = mock(Date.class);
        when(dateFactory.now()).thenReturn(currentDate);
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
                    .bodyValue(ResourceUtils.resourceAsString("account/api/opening/open_account.request.json"))
                    .exchange()
                    .expectStatus()
                    .is2xxSuccessful();

            verify(commandGateway).sendAndWait(OpenAccountCommand.builder()
                    .name("account-name")
                    .id(ACCOUNT_ID)
                    .currencyCode(CurrencyCode.USD)
                    .budgetInitialAmount(BigDecimal.valueOf(81.0))
                    .timestamp(currentDate).build());
        }

        @Test
        @DisplayName("it should return the account id")
        void it_should_return_the_account_id() {
            client.post()
                    .uri("/accounts/open")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(ResourceUtils.resourceAsString("account/api/opening/open_account.request.json"))
                    .exchange()
                    .expectBody()
                    .json(ResourceUtils.resourceAsString("account/api/opening/open_account.response.json"));
        }
    }

}