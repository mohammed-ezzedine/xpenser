package me.ezzedine.mohammed.xpenser.core.account;

import me.ezzedine.mohammed.xpenser.core.account.budget.Budget;
import me.ezzedine.mohammed.xpenser.core.account.budget.Currencies;
import me.ezzedine.mohammed.xpenser.core.account.budget.Currency;
import me.ezzedine.mohammed.xpenser.core.account.budget.CurrencyCode;
import me.ezzedine.mohammed.xpenser.core.account.opening.AccountOpenedEvent;
import me.ezzedine.mohammed.xpenser.core.account.opening.OpenAccountCommand;
import me.ezzedine.mohammed.xpenser.core.account.transactions.*;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class AccountAggregateTest {

    public static final String ACCOUNT_NAME = UUID.randomUUID().toString();
    public static final String ACCOUNT_ID = UUID.randomUUID().toString();
    public static final BigDecimal INITIAL_AMOUNT = BigDecimal.valueOf(10);

    public static final String TRANSACTION_ID = UUID.randomUUID().toString();
    private final AggregateTestFixture<AccountAggregate> testFixture = new AggregateTestFixture<>(AccountAggregate.class);
    private Date timestamp;

    @BeforeEach
    void setUp() {
        timestamp = mock(Date.class);
    }

    @Test
    @DisplayName("it should throw an error upon receiving an open account command with a negative initial budget")
    void it_should_throw_an_error_upon_receiving_an_open_account_command_with_a_negative_initial_budget() {
        testFixture.givenNoPriorActivity()
                .when(new OpenAccountCommand(ACCOUNT_ID, ACCOUNT_NAME, CurrencyCode.LEBANESE_LIRA.getValue(), BigDecimal.valueOf(-3), timestamp))
                .expectException(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("it should publish an account opened event upon receiving an open account command")
    void it_should_publish_an_account_opened_event_upon_receiving_an_open_account_command() {
        BigDecimal budgetInitialAmount = BigDecimal.valueOf(10);
        testFixture.givenNoPriorActivity()
                .when(OpenAccountCommand.builder().name(ACCOUNT_NAME).id(ACCOUNT_ID).currencyCode(CurrencyCode.DOLLAR.getValue()).budgetInitialAmount(budgetInitialAmount).timestamp(timestamp).build())
                .expectSuccessfulHandlerExecution()
                .expectEvents(new AccountOpenedEvent(ACCOUNT_ID, ACCOUNT_NAME, Budget.builder().currency(Currencies.dollar()).amount(budgetInitialAmount).build(), timestamp));
    }

    @Test
    @DisplayName("it should throw an exception when receiving a debit money command with a negative amount")
    void it_should_throw_an_exception_when_receiving_a_debit_money_command_with_a_negative_amount() {
        testFixture.given(accountOpenedEvent())
                .when(new DepositMoneyCommand(TRANSACTION_ID, ACCOUNT_ID, BigDecimal.valueOf(-4), "", mock(Date.class)))
                .expectException(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("it should throw an exception when receiving a debit money command with a zero amount")
    void it_should_throw_an_exception_when_receiving_a_debit_money_command_with_a_zero_amount() {
        testFixture.given(accountOpenedEvent())
                .when(new DepositMoneyCommand(TRANSACTION_ID, ACCOUNT_ID, BigDecimal.ZERO, "", mock(Date.class)))
                .expectException(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("it should publish a debit transaction initiated event upon receiving a debit money command")
    void it_should_publish_a_debit_transaction_initiated_event_upon_receiving_a_debit_money_command() {
        BigDecimal debitAmount = BigDecimal.valueOf(new Random().nextDouble(1, 10));
        testFixture.given(accountOpenedEvent())
                .when(new DepositMoneyCommand(TRANSACTION_ID, ACCOUNT_ID, debitAmount, "transaction message", timestamp))
                .expectEvents(new MoneyDepositedInAccountEvent(TRANSACTION_ID, ACCOUNT_ID, debitAmount, "transaction message", timestamp));
    }

    @Test
    @DisplayName("it should add the new amount to the budget upon receiving money added to account event")
    void it_should_add_the_new_amount_to_the_budget_upon_receiving_money_added_to_account_event() {
        testFixture.given(accountOpenedEvent())
                .when(new DepositMoneyCommand(TRANSACTION_ID, ACCOUNT_ID, BigDecimal.valueOf(5), "", mock(Date.class)))
                .expectSuccessfulHandlerExecution()
                .expectState(account -> assertEquals(BigDecimal.valueOf(15), account.getBudget().getAmount()));
    }

    @Test
    @DisplayName("it should throw an exception upon receiving a withdraw money command with a negative amount value")
    void it_should_throw_an_exception_upon_receiving_a_withdraw_money_command_with_a_negative_amount_value() {
        testFixture.given(accountOpenedEvent())
                .when(new WithdrawMoneyCommand(TRANSACTION_ID, ACCOUNT_ID, BigDecimal.valueOf(-1), "", mock(Date.class)))
                .expectException(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("it should throw an exception upon receiving a withdraw money command with a zero amount value")
    void it_should_throw_an_exception_upon_receiving_a_withdraw_money_command_with_a_zero_amount_value() {
        testFixture.given(accountOpenedEvent())
                .when(new WithdrawMoneyCommand(TRANSACTION_ID, ACCOUNT_ID, BigDecimal.ZERO, "", mock(Date.class)))
                .expectException(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("it should throw an exception upon receiving a withdraw money command with a value greater than the available balance")
    void it_should_throw_an_exception_upon_receiving_a_withdraw_money_command_with_a_value_greater_than_the_available_balance() {
        testFixture.given(accountOpenedEvent())
                .when(new WithdrawMoneyCommand(TRANSACTION_ID, ACCOUNT_ID, INITIAL_AMOUNT.add(BigDecimal.valueOf(1)), "", mock(Date.class)))
                .expectException(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("it should issue a money withdrew from account event upon receiving a withdraw money command with a valid requested amount")
    void it_should_issue_a_money_withdrew_from_account_event_upon_receiving_a_withdraw_money_command_with_a_valid_requested_amount() {
        testFixture.given(accountOpenedEvent())
                .when(new WithdrawMoneyCommand(TRANSACTION_ID, ACCOUNT_ID, BigDecimal.valueOf(9), "transaction summary", timestamp))
                .expectSuccessfulHandlerExecution()
                .expectEvents(new MoneyWithdrewFromAccountEvent(TRANSACTION_ID, ACCOUNT_ID, BigDecimal.valueOf(9), "transaction summary", timestamp));
    }

    @Test
    @DisplayName("it should subtract the request amount from the budget upon receiving a withdraw money command with")
    void it_should_subtract_the_request_amount_from_the_budget_upon_receiving_a_withdraw_money_command_with() {
        testFixture.given(accountOpenedEvent())
                .when(new WithdrawMoneyCommand(TRANSACTION_ID, ACCOUNT_ID, BigDecimal.valueOf(9), "transaction summary", timestamp))
                .expectSuccessfulHandlerExecution()
                .expectState(acc -> assertEquals(BigDecimal.valueOf(1), acc.getBudget().getAmount()));
    }

    @Test
    @DisplayName("it should throw an exception upon receiving a transfer money command with a negative requested amount")
    void it_should_throw_an_exception_upon_receiving_a_transfer_money_command_with_a_negative_requested_amount() {
        testFixture.given(accountOpenedEvent())
                .when(new TransferMoneyCommand(ACCOUNT_ID, UUID.randomUUID().toString(), UUID.randomUUID().toString(), BigDecimal.valueOf(-1), mock(Date.class)))
                .expectException(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("it should throw an exception upon receiving a transfer money command with a zero requested amount")
    void it_should_throw_an_exception_upon_receiving_a_transfer_money_command_with_a_zero_requested_amount() {
        testFixture.given(accountOpenedEvent())
                .when(new TransferMoneyCommand(ACCOUNT_ID, UUID.randomUUID().toString(), UUID.randomUUID().toString(), BigDecimal.ZERO, mock(Date.class)))
                .expectException(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("it should throw an exception upon receiving a transfer money command with a request amount bigger than the available balance")
    void it_should_throw_an_exception_upon_receiving_a_transfer_money_command_with_a_request_amount_bigger_than_the_available_balance() {
        testFixture.given(accountOpenedEvent())
                .when(new TransferMoneyCommand(ACCOUNT_ID, UUID.randomUUID().toString(), UUID.randomUUID().toString(), BigDecimal.valueOf(20), mock(Date.class)))
                .expectException(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("it should issue a money transfer initiated event upon receiving a transfer money command with a valid request amount")
    void it_should_issue_a_money_transfer_initiated_event_upon_receiving_a_transfer_money_command_with_a_valid_request_amount() {
        String destinationAccountId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();
        testFixture.given(accountOpenedEvent())
                .when(new TransferMoneyCommand(ACCOUNT_ID, destinationAccountId, transactionId, BigDecimal.valueOf(5), timestamp))
                .expectSuccessfulHandlerExecution()
                .expectEvents(new MoneyTransferInitiatedEvent(transactionId, ACCOUNT_ID, destinationAccountId, BigDecimal.valueOf(5), timestamp));
    }

    private AccountOpenedEvent accountOpenedEvent() {
        return new AccountOpenedEvent(ACCOUNT_ID, ACCOUNT_NAME, new Budget(new Currency(CurrencyCode.EURO, "EUR", "Euro"), INITIAL_AMOUNT), timestamp);
    }
}