package me.ezzedine.mohammed.xpenser.api.account.transactions;

import me.ezzedine.mohammed.xpenser.api.account.ResourceUtils;
import me.ezzedine.mohammed.xpenser.core.account.transactions.*;
import me.ezzedine.mohammed.xpenser.utils.TransactionUtils;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(AccountTransactionsController.class)
class AccountTransactionsControllerIntegrationTest {

    @Autowired
    private WebTestClient testClient;

    @MockBean
    private CommandGateway commandGateway;
    @MockBean
    private DateFactory dateFactory;
    @MockBean
    private TransactionIdGenerator transactionIdGenerator;

    @BeforeEach
    void setUp() {
        when(dateFactory.now()).thenReturn(TransactionUtils.TRANSACTION_DATE);
        when(commandGateway.sendAndWait(any())).thenReturn(CompletableFuture.completedFuture(null));
        when(transactionIdGenerator.generate()).thenReturn(Mono.just(TransactionUtils.TRANSACTION_ID));
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

        DepositMoneyCommand command = TransactionUtils.depositMoneyCommand().accountId("account-id").amount(BigDecimal.valueOf(10))
                .note("message").build();
        verify(commandGateway).sendAndWait(command);
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

        WithdrawMoneyCommand command = TransactionUtils.withdrawMoneyCommand().accountId("account-id").amount(BigDecimal.valueOf(10))
                .note("message").build();
        verify(commandGateway).sendAndWait(command);
    }

    @Test
    @DisplayName("it should issue a transfer money command when the user sends a request to transfer money from an account to another")
    void it_should_issue_a_transfer_money_command_when_the_user_sends_a_request_to_transfer_money_from_an_account_to_another() {
        testClient.post()
                .uri("/accounts/account-id/transactions/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ResourceUtils.resourceAsString("account/api/transactions/transfer_money.request.json"))
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        TransferMoneyCommand command = TransactionUtils.transferMoneyCommand().sourceAccountId("account-id")
                .destinationAccountId("destination-account-id").amount(BigDecimal.valueOf(10)).build();
        verify(commandGateway).sendAndWait(command);
    }


}