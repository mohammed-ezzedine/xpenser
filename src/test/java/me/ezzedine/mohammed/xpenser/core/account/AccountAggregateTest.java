package me.ezzedine.mohammed.xpenser.core.account;

import me.ezzedine.mohammed.xpenser.core.currency.exchange.CurrencyExchangeManager;
import me.ezzedine.mohammed.xpenser.utils.AccountUtils;
import me.ezzedine.mohammed.xpenser.utils.BudgetUtils;
import me.ezzedine.mohammed.xpenser.utils.CurrencyUtils;
import me.ezzedine.mohammed.xpenser.utils.TransactionUtils;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AccountAggregateTest {
    private final AggregateTestFixture<AccountAggregate> testFixture = new AggregateTestFixture<>(AccountAggregate.class);

    @BeforeEach
    void setUp() {
        CurrencyExchangeManager currencyExchangeManager = mock(CurrencyExchangeManager.class);
        testFixture.registerInjectableResource(currencyExchangeManager);

        when(currencyExchangeManager.convert(TransactionUtils.TRANSACTION_AMOUNT, CurrencyUtils.anotherCurrencyCode(), CurrencyUtils.currencyCode()))
                .thenReturn(TransactionUtils.ANOTHER_TRANSACTION_AMOUNT);
    }

    @Test
    @DisplayName("it should throw an exception when receiving a deposit money command with a negative amount")
    void it_should_throw_an_exception_when_receiving_a_deposit_money_command_with_a_negative_amount() {
        testFixture.given(AccountUtils.accountOpenedEvent())
                .when(TransactionUtils.depositMoneyCommand().amount(BigDecimal.valueOf(-1)).build())
                .expectException(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("it should throw an exception when receiving a deposit money command with a zero amount")
    void it_should_throw_an_exception_when_receiving_a_deposit_money_command_with_a_zero_amount() {
        testFixture.given(AccountUtils.accountOpenedEvent())
                .when(TransactionUtils.depositMoneyCommand().amount(BigDecimal.ZERO).build())
                .expectException(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("it should publish a money deposited event upon receiving a debit money command")
    void it_should_publish_a_money_deposited_event_upon_receiving_a_debit_money_command() {
        testFixture.given(AccountUtils.accountOpenedEvent())
                .when(TransactionUtils.depositMoneyCommand().build())
                .expectEvents(TransactionUtils.moneyDepositedIntoAccountEvent().build());
    }

    @Test
    @DisplayName("it should publish a money deposited event after converting the amount upon receiving a debit money command with a currency code specified")
    void it_should_publish_a_money_deposited_event_after_converting_the_amount_upon_receiving_a_debit_money_command_with_a_currency_code_specified() {
        testFixture.given(AccountUtils.accountOpenedEvent())
                .when(TransactionUtils.depositMoneyCommand().currencyCode(CurrencyUtils.anotherCurrencyCode()).build())
                .expectEvents(TransactionUtils.moneyDepositedIntoAccountEvent().amount(TransactionUtils.ANOTHER_TRANSACTION_AMOUNT).build());
    }

    @Test
    @DisplayName("it should add the new amount to the budget upon receiving money added to account event")
    void it_should_add_the_new_amount_to_the_budget_upon_receiving_money_added_to_account_event() {
        testFixture.given(AccountUtils.accountOpenedEvent())
                .when(TransactionUtils.depositMoneyCommand().build())
                .expectSuccessfulHandlerExecution()
                .expectState(account -> assertEquals(BudgetUtils.BUDGET_AMOUNT.add(TransactionUtils.TRANSACTION_AMOUNT), account.getBudget().getAmount()));
    }

    @Test
    @DisplayName("it should throw an exception upon receiving a withdraw money command with a negative amount value")
    void it_should_throw_an_exception_upon_receiving_a_withdraw_money_command_with_a_negative_amount_value() {
        testFixture.given(AccountUtils.accountOpenedEvent())
                .when(TransactionUtils.withdrawMoneyCommand().amount(BigDecimal.valueOf(-1)).build())
                .expectException(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("it should throw an exception upon receiving a withdraw money command with a zero amount value")
    void it_should_throw_an_exception_upon_receiving_a_withdraw_money_command_with_a_zero_amount_value() {
        testFixture.given(AccountUtils.accountOpenedEvent())
                .when(TransactionUtils.withdrawMoneyCommand().amount(BigDecimal.ZERO).build())
                .expectException(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("it should throw an exception upon receiving a withdraw money command with a value greater than the available balance")
    void it_should_throw_an_exception_upon_receiving_a_withdraw_money_command_with_a_value_greater_than_the_available_balance() {
        testFixture.given(AccountUtils.accountOpenedEvent())
                .when(TransactionUtils.withdrawMoneyCommand().amount(BudgetUtils.BUDGET_AMOUNT.add(BigDecimal.ONE)).build())
                .expectException(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("it should issue a money withdrew from account event upon receiving a withdraw money command with a valid requested amount")
    void it_should_issue_a_money_withdrew_from_account_event_upon_receiving_a_withdraw_money_command_with_a_valid_requested_amount() {
        testFixture.given(AccountUtils.accountOpenedEvent())
                .when(TransactionUtils.withdrawMoneyCommand().build())
                .expectSuccessfulHandlerExecution()
                .expectEvents(TransactionUtils.moneyWithdrewFromAccountEvent().build());
    }

    @Test
    @DisplayName("it should subtract the request amount from the budget upon receiving a withdraw money command with")
    void it_should_subtract_the_request_amount_from_the_budget_upon_receiving_a_withdraw_money_command_with() {
        testFixture.given(AccountUtils.accountOpenedEvent())
                .when(TransactionUtils.withdrawMoneyCommand().build())
                .expectSuccessfulHandlerExecution()
                .expectState(acc -> assertEquals(BudgetUtils.BUDGET_AMOUNT.subtract(TransactionUtils.TRANSACTION_AMOUNT), acc.getBudget().getAmount()));
    }

    @Test
    @DisplayName("it should throw an exception upon receiving a transfer money command with a negative requested amount")
    void it_should_throw_an_exception_upon_receiving_a_transfer_money_command_with_a_negative_requested_amount() {
        testFixture.given(AccountUtils.accountOpenedEvent())
                .when(TransactionUtils.transferMoneyCommand().amount(BigDecimal.valueOf(-1)).build())
                .expectException(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("it should throw an exception upon receiving a transfer money command with a zero requested amount")
    void it_should_throw_an_exception_upon_receiving_a_transfer_money_command_with_a_zero_requested_amount() {
        testFixture.given(AccountUtils.accountOpenedEvent())
                .when(TransactionUtils.transferMoneyCommand().amount(BigDecimal.ZERO).build())
                .expectException(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("it should throw an exception upon receiving a transfer money command with a request amount bigger than the available balance")
    void it_should_throw_an_exception_upon_receiving_a_transfer_money_command_with_a_request_amount_bigger_than_the_available_balance() {
        testFixture.given(AccountUtils.accountOpenedEvent())
                .when(TransactionUtils.transferMoneyCommand().amount(BudgetUtils.BUDGET_AMOUNT.add(BigDecimal.ONE)).build())
                .expectException(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("it should issue a money transfer initiated event upon receiving a transfer money command with a valid request amount")
    void it_should_issue_a_money_transfer_initiated_event_upon_receiving_a_transfer_money_command_with_a_valid_request_amount() {
        testFixture.given(AccountUtils.accountOpenedEvent())
                .when(TransactionUtils.transferMoneyCommand().build())
                .expectSuccessfulHandlerExecution()
                .expectEvents(TransactionUtils.moneyTransferInitiatedEvent().build());
    }

}