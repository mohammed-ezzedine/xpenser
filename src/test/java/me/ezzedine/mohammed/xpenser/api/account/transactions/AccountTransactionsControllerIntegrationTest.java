package me.ezzedine.mohammed.xpenser.api.account.transactions;

import me.ezzedine.mohammed.xpenser.api.account.ResourceUtils;
import me.ezzedine.mohammed.xpenser.core.account.transactions.DateFactory;
import me.ezzedine.mohammed.xpenser.core.account.transactions.DepositMoneyCommand;
import me.ezzedine.mohammed.xpenser.core.account.transactions.WithdrawMoneyCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
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
        when(commandGateway.send(any())).thenReturn(CompletableFuture.completedFuture(null));
    }

    @Test
    @DisplayName("it should issue a deposit money command when the user deposits money in an account")
    void it_should_issue_a_deposit_money_command_when_the_user_deposits_money_in_an_account() {
        testClient.post()
                .uri("/accounts/account-id/transactions/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ResourceUtils.resourceAsString("account/api/transactions/deposit_money.request.json"))
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        verify(commandGateway).send(new DepositMoneyCommand("account-id", BigDecimal.valueOf(10), "message", currentDate));
    }

    @Test
    @DisplayName("it should issue a withdraw money command when the user withdraws money from an account")
    void it_should_issue_a_withdraw_money_command_when_the_user_withdraws_money_in_an_account() {
        testClient.post()
                .uri("/accounts/account-id/transactions/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ResourceUtils.resourceAsString("account/api/transactions/deposit_money.request.json"))
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        verify(commandGateway).send(new WithdrawMoneyCommand("account-id", BigDecimal.valueOf(10), "message", currentDate));
    }


}