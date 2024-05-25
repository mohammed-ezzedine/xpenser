package me.ezzedine.mohammed.xpenser.api.account.transactions;

import me.ezzedine.mohammed.xpenser.api.account.ResourceUtils;
import me.ezzedine.mohammed.xpenser.core.account.transactions.DateFactory;
import me.ezzedine.mohammed.xpenser.core.account.transactions.DepositMoneyCommand;
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

import java.util.Date;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebFluxTest(AccountTransactionsController.class)
class AccountTransactionsControllerIntegrationTest {

    @Autowired
    private WebTestClient testClient;

    @MockBean
    private CommandGateway commandGateway;

    @MockBean
    private DateFactory dateFactory;
    private Date currentDate;

    @BeforeEach
    void setUp() {
        currentDate = mock(Date.class);
        when(dateFactory.now()).thenReturn(currentDate);
    }

    @Nested
    @DisplayName("When the user deposits money in an account")
    class DepositMoney {

        @BeforeEach
        void setUp() {
            when(commandGateway.send(any())).thenReturn(CompletableFuture.completedFuture(null));
        }

        @Test
        @DisplayName("it should issue a deposit money command")
        void it_should_issue_a_deposit_money_command() {
            testClient.post()
                    .uri("/accounts/account-id/transactions/deposit")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(ResourceUtils.resourceAsString("account/api/transactions/deposit_money.request.json"))
                    .exchange()
                    .expectStatus()
                    .is2xxSuccessful();

            verify(commandGateway).send(new DepositMoneyCommand("account-id", 10, currentDate));
        }
    }
}