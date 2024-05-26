package me.ezzedine.mohammed.xpenser.core.account.transactions;

import org.axonframework.test.saga.SagaTestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static org.mockito.Mockito.mock;

class TransferMoneySagaTest {

    public static final String TRANSACTION_ID = UUID.randomUUID().toString();
    public static final String SOURCE_ACCOUNT_ID = UUID.randomUUID().toString();
    public static final String DESTINATION_ACCOUNT_ID = UUID.randomUUID().toString();
    public static final Date TIMESTAMP = mock(Date.class);
    public static final BigDecimal AMOUNT = mock(BigDecimal.class);
    private final SagaTestFixture<TransferMoneySaga> testFixture = new SagaTestFixture<>(TransferMoneySaga.class);

    @Test
    @DisplayName("it should issue a withdraw money command from the source account upon receiving a money transfer initiated event")
    void it_should_issue_a_withdraw_money_command_from_the_source_account_upon_receiving_a_money_transfer_initiated_event() {
        testFixture.givenNoPriorActivity()
                .whenAggregate(SOURCE_ACCOUNT_ID)
                .publishes(new MoneyTransferInitiatedEvent(TRANSACTION_ID, SOURCE_ACCOUNT_ID, DESTINATION_ACCOUNT_ID, AMOUNT, TIMESTAMP))
                .expectAssociationWith("transactionId", TRANSACTION_ID)
                .expectDispatchedCommands(new WithdrawMoneyCommand(TRANSACTION_ID, SOURCE_ACCOUNT_ID, AMOUNT, "Transfer to account " + DESTINATION_ACCOUNT_ID, TIMESTAMP));
    }

    @Test
    @DisplayName("it should issue a deposit money command into the destination account upon receiving a money withdrew from the source account event")
    void it_should_issue_a_deposit_money_command_into_the_destination_account_upon_receiving_a_money_withdrew_from_the_source_account_event() {
        testFixture.givenAggregate(SOURCE_ACCOUNT_ID)
                .published(new MoneyTransferInitiatedEvent(TRANSACTION_ID, SOURCE_ACCOUNT_ID, DESTINATION_ACCOUNT_ID, AMOUNT, TIMESTAMP))
                .whenPublishingA(new MoneyWithdrewFromAccountEvent(TRANSACTION_ID, SOURCE_ACCOUNT_ID, AMOUNT, "Transfer to account " + DESTINATION_ACCOUNT_ID, TIMESTAMP))
                .expectDispatchedCommands(new DepositMoneyCommand(TRANSACTION_ID, DESTINATION_ACCOUNT_ID, AMOUNT, "Transfer from account " + SOURCE_ACCOUNT_ID, TIMESTAMP));
    }

    @Test
    @DisplayName("it should end upon receiving a money deposited event")
    void it_should_end_upon_receiving_a_money_deposited_event() {
        testFixture.givenAggregate(SOURCE_ACCOUNT_ID)
                .published(new MoneyTransferInitiatedEvent(TRANSACTION_ID, SOURCE_ACCOUNT_ID, DESTINATION_ACCOUNT_ID, AMOUNT, TIMESTAMP))
                .whenPublishingA(new MoneyDepositedInAccountEvent(TRANSACTION_ID, DESTINATION_ACCOUNT_ID, AMOUNT, "Transfer from account " + SOURCE_ACCOUNT_ID, TIMESTAMP))
                .expectActiveSagas(0);
    }
}