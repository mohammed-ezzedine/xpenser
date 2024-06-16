package me.ezzedine.mohammed.xpenser.core.account.transactions;

import me.ezzedine.mohammed.xpenser.utils.AccountUtils;
import me.ezzedine.mohammed.xpenser.utils.CurrencyUtils;
import me.ezzedine.mohammed.xpenser.utils.TransactionUtils;
import org.axonframework.test.saga.SagaTestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TransferMoneySagaTest {

    private final SagaTestFixture<TransferMoneySaga> testFixture = new SagaTestFixture<>(TransferMoneySaga.class);

    @Test
    @DisplayName("it should issue a withdraw money command from the source account upon receiving a money transfer initiated event")
    void it_should_issue_a_withdraw_money_command_from_the_source_account_upon_receiving_a_money_transfer_initiated_event() {
        testFixture.givenNoPriorActivity()
                .whenAggregate(AccountUtils.ACCOUNT_ID)
                .publishes(TransactionUtils.moneyTransferInitiatedEvent().build())
                .expectAssociationWith("transactionId", TransactionUtils.TRANSACTION_ID)
                .expectDispatchedCommands(TransactionUtils.withdrawMoneyCommand().note("Internal Transfer").category(null).build());
    }

    @Test
    @DisplayName("it should issue a deposit money command into the destination account upon receiving a money withdrew from the source account event")
    void it_should_issue_a_deposit_money_command_into_the_destination_account_upon_receiving_a_money_withdrew_from_the_source_account_event() {
        testFixture.givenAggregate(AccountUtils.ACCOUNT_ID)
                .published(TransactionUtils.moneyTransferInitiatedEvent().build())
                .whenPublishingA(TransactionUtils.moneyWithdrewFromAccountEvent().note("Internal Transfer").category(null).build())
                .expectDispatchedCommands(TransactionUtils.depositMoneyCommand()
                        .accountId(AccountUtils.ANOTHER_ACCOUNT_ID)
                        .note("Internal Transfer")
                        .currencyCode(CurrencyUtils.currencyCode())
                        .build());
    }

    @Test
    @DisplayName("it should end upon receiving a money deposited event")
    void it_should_end_upon_receiving_a_money_deposited_event() {
        testFixture.givenAggregate(AccountUtils.ACCOUNT_ID)
                .published(TransactionUtils.moneyTransferInitiatedEvent().build())
                .whenPublishingA(TransactionUtils.moneyDepositedIntoAccountEvent().accountId(AccountUtils.ANOTHER_ACCOUNT_ID).note("Internal Transfer").build())
                .expectActiveSagas(0);
    }
}