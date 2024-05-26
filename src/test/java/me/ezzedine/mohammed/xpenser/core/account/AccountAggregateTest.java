package me.ezzedine.mohammed.xpenser.core.account;

import me.ezzedine.mohammed.xpenser.core.account.budget.Budget;
import me.ezzedine.mohammed.xpenser.core.account.budget.Currencies;
import me.ezzedine.mohammed.xpenser.core.account.budget.Currency;
import me.ezzedine.mohammed.xpenser.core.account.budget.CurrencyCode;
import me.ezzedine.mohammed.xpenser.core.account.opening.AccountOpenedEvent;
import me.ezzedine.mohammed.xpenser.core.account.opening.OpenAccountCommand;
import me.ezzedine.mohammed.xpenser.core.account.transactions.DepositMoneyCommand;
import me.ezzedine.mohammed.xpenser.core.account.transactions.MoneyDepositedInAccountEvent;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class AccountAggregateTest {

    public static final String ACCOUNT_NAME = UUID.randomUUID().toString();
    public static final String ACCOUNT_ID = UUID.randomUUID().toString();
    public static final int INITIAL_AMOUNT = 10;
    public static final Currency CURRENCY = mock(Currency.class);
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
                .when(new OpenAccountCommand(ACCOUNT_ID, ACCOUNT_NAME, CurrencyCode.LEBANESE_LIRA.getValue(), -3, timestamp))
                .expectException(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("it should publish an account opened event upon receiving an open account command")
    void it_should_publish_an_account_opened_event_upon_receiving_an_open_account_command() {
        double budgetInitialAmount = 10;
        testFixture.givenNoPriorActivity()
                .when(OpenAccountCommand.builder().name(ACCOUNT_NAME).id(ACCOUNT_ID).currencyCode(CurrencyCode.DOLLAR.getValue()).budgetInitialAmount(budgetInitialAmount).timestamp(timestamp).build())
                .expectSuccessfulHandlerExecution()
                .expectEvents(new AccountOpenedEvent(ACCOUNT_ID, ACCOUNT_NAME, Budget.builder().currency(Currencies.dollar()).amount(budgetInitialAmount).build(), timestamp));
    }

    @Test
    @DisplayName("it should throw an exception when receiving a debit money command with a negative amount")
    void it_should_throw_an_exception_when_receiving_a_debit_money_command_with_a_negative_amount() {
        testFixture.given(accountOpenedEvent())
                .when(new DepositMoneyCommand(ACCOUNT_ID, -4, mock(Date.class)))
                .expectException(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("it should publish a debit transaction initiated event upon receiving a debit money command")
    void it_should_publish_a_debit_transaction_initiated_event_upon_receiving_a_debit_money_command() {
        double debitAmount = new Random().nextDouble(1, 10);
        testFixture.given(accountOpenedEvent())
                .when(new DepositMoneyCommand(ACCOUNT_ID, debitAmount, timestamp))
                .expectEvents(new MoneyDepositedInAccountEvent(ACCOUNT_ID, debitAmount, timestamp));
    }

    @Test
    @DisplayName("it should add the new amount to the budget upon receiving money added to account event")
    void it_should_add_the_new_amount_to_the_budget_upon_receiving_money_added_to_account_event() {
        testFixture.given(accountOpenedEvent())
                .when(new DepositMoneyCommand(ACCOUNT_ID, 5, mock(Date.class)))
                .expectSuccessfulHandlerExecution()
                .expectState(account -> assertEquals(15, account.getBudget().getAmount()));
    }

    @NotNull
    private AccountOpenedEvent accountOpenedEvent() {
        return new AccountOpenedEvent(ACCOUNT_ID, ACCOUNT_NAME, new Budget(CURRENCY, INITIAL_AMOUNT), timestamp);
    }
}