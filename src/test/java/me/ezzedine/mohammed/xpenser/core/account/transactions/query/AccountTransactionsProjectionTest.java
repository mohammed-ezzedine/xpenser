package me.ezzedine.mohammed.xpenser.core.account.transactions.query;

import me.ezzedine.mohammed.xpenser.core.account.budget.Budget;
import me.ezzedine.mohammed.xpenser.core.account.budget.Currency;
import me.ezzedine.mohammed.xpenser.core.account.opening.AccountOpenedEvent;
import me.ezzedine.mohammed.xpenser.core.account.transactions.MoneyDepositedInAccountEvent;
import me.ezzedine.mohammed.xpenser.core.account.transactions.MoneyWithdrewFromAccountEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class AccountTransactionsProjectionTest {

    private AccountTransactionsProjection projection;

    @BeforeEach
    void setUp() {
        projection = new AccountTransactionsProjection();
    }

    @Test
    @DisplayName("it should save a transaction when an account is opened")
    void it_should_save_a_transaction_when_an_account_is_opened() {
        Date timestamp = mock(Date.class);
        projection.on(new AccountOpenedEvent("account-id", UUID.randomUUID().toString(), new Budget(mock(Currency.class), 10), timestamp));
        List<TransactionSummary> transactions = projection.handle(new FetchAccountTransactionsQuery("account-id"));
        assertEquals(1, transactions.size());
        assertEquals(10, transactions.getFirst().amount());
        assertEquals(10, transactions.getFirst().balance());
        assertEquals("Account opening", transactions.getFirst().note());
        assertEquals(timestamp, transactions.getFirst().timestamp());
    }

    @Test
    @DisplayName("it should save a transaction when money is deposited into an account")
    void it_should_save_a_transaction_when_money_is_deposited_into_an_account() {
        projection.on(new AccountOpenedEvent("account-id", UUID.randomUUID().toString(), new Budget(mock(Currency.class), 10), mock(Date.class)));
        Date timestamp = mock(Date.class);
        projection.on(new MoneyDepositedInAccountEvent("account-id", 5.3, "note", timestamp));
        List<TransactionSummary> transactions = projection.handle(new FetchAccountTransactionsQuery("account-id"));
        assertEquals(2, transactions.size());
        assertEquals(5.3, transactions.getFirst().amount());
        assertEquals(15.3, transactions.getFirst().balance());
        assertEquals("note", transactions.getFirst().note());
        assertEquals(timestamp, transactions.getFirst().timestamp());
    }

    @Test
    @DisplayName("it should save a transaction when money is withdrew from an account")
    void it_should_save_a_transaction_when_money_is_withdrew_from_an_account() {
        projection.on(new AccountOpenedEvent("account-id", UUID.randomUUID().toString(), new Budget(mock(Currency.class), 10), mock(Date.class)));
        Date timestamp = mock(Date.class);
        projection.on(new MoneyWithdrewFromAccountEvent("account-id", 5.3, "note", timestamp));
        List<TransactionSummary> transactions = projection.handle(new FetchAccountTransactionsQuery("account-id"));
        assertEquals(2, transactions.size());
        assertEquals(5.3, transactions.getFirst().amount());
        assertEquals(4.7, transactions.getFirst().balance());
        assertEquals("note", transactions.getFirst().note());
        assertEquals(timestamp, transactions.getFirst().timestamp());
    }
}