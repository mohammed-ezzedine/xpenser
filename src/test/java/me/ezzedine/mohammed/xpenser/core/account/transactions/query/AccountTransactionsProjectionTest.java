package me.ezzedine.mohammed.xpenser.core.account.transactions.query;

import me.ezzedine.mohammed.xpenser.core.account.AccountNotFoundException;
import me.ezzedine.mohammed.xpenser.utils.AccountUtils;
import me.ezzedine.mohammed.xpenser.utils.BudgetUtils;
import me.ezzedine.mohammed.xpenser.utils.ExpenseCategoryUtils;
import me.ezzedine.mohammed.xpenser.utils.TransactionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

class AccountTransactionsProjectionTest {

    private final AccountTransactionsStorage storage = mock(AccountTransactionsStorage.class);
    private AccountTransactionsProjection projection;

    @BeforeEach
    void setUp() {
        projection = new AccountTransactionsProjection(storage, new AccountTransactionMapperImpl());
        when(storage.save(any())).thenReturn(Mono.empty());
    }

    @Test
    @DisplayName("it should save a transaction when an account is opened")
    void it_should_save_a_transaction_when_an_account_is_opened() {
        projection.on(AccountUtils.accountOpenedEvent());

        verify(storage).save(AccountUtils.accountTransactionsSummary().transactions(List.of(
                TransactionUtils.openingTransactionSummary().build())).build());
    }

    @Test
    @DisplayName("it should save a transaction when money is deposited into an account")
    void it_should_save_a_transaction_when_money_is_deposited_into_an_account() {
        when(storage.findById(AccountUtils.ACCOUNT_ID)).thenReturn(Mono.just(AccountUtils.accountTransactionsSummary().transactions(List.of(TransactionUtils.anotherTransactionSummary().build())).build()));

        projection.on(TransactionUtils.moneyDepositedIntoAccountEvent().build());

        verify(storage).save(AccountUtils.accountTransactionsSummary()
                .transactions(
                        List.of(
                            TransactionUtils.transactionSummary()
                                    .balance(BudgetUtils.ANOTHER_BUDGET_AMOUNT.add(TransactionUtils.TRANSACTION_AMOUNT))
                                    .category(null)
                                    .build(),
                            TransactionUtils.anotherTransactionSummary().build()
                        )
                )
                .build());
    }

    @Test
    @DisplayName("it should save a transaction when money is withdrew from an account")
    void it_should_save_a_transaction_when_money_is_withdrew_from_an_account() {
        when(storage.findById(AccountUtils.ACCOUNT_ID)).thenReturn(Mono.just(AccountUtils.accountTransactionsSummary().transactions(List.of(TransactionUtils.anotherTransactionSummary().build())).build()));

        projection.on(TransactionUtils.moneyWithdrewFromAccountEvent().build());

        verify(storage).save(AccountUtils.accountTransactionsSummary()
                .transactions(
                        List.of(
                                TransactionUtils.transactionSummary()
                                        .amount(TransactionUtils.TRANSACTION_AMOUNT.negate())
                                        .balance(BudgetUtils.ANOTHER_BUDGET_AMOUNT.subtract(TransactionUtils.TRANSACTION_AMOUNT))
                                        .category(ExpenseCategoryUtils.EXPENSE_CATEGORY_ID)
                                        .build(),
                                TransactionUtils.anotherTransactionSummary().build()
                        )
                )
                .build());
    }

    @Test
    @DisplayName("it should fetch the account transactions from the store when a fetch account transactions query is issued")
    void it_should_fetch_the_account_transactions_from_the_store_when_a_fetch_account_transactions_query_is_issued() {
        when(storage.findById(AccountUtils.ACCOUNT_ID)).thenReturn(Mono.just(AccountUtils.accountTransactionsSummary().build()));

        Flux<TransactionSummary> accountTransactionsFlux = projection.handle(new FetchAccountTransactionsQuery(AccountUtils.ACCOUNT_ID));

        StepVerifier.create(accountTransactionsFlux)
                .expectNext(TransactionUtils.transactionSummary().build())
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("it should throw an exception if the account does not exist when a fetch account transactions query is issued")
    void it_should_throw_an_exception_if_the_account_does_not_exist_when_a_fetch_account_transactions_query_is_issued() {
        when(storage.findById(AccountUtils.ACCOUNT_ID)).thenReturn(Mono.empty());

        Flux<TransactionSummary> accountTransactionsFlux = projection.handle(new FetchAccountTransactionsQuery(AccountUtils.ACCOUNT_ID));

        StepVerifier.create(accountTransactionsFlux)
                .expectError(AccountNotFoundException.class)
                .verify();
    }
}