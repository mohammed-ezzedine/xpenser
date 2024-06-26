package me.ezzedine.mohammed.xpenser.core.account.summary;

import me.ezzedine.mohammed.xpenser.core.account.AccountNotFoundException;
import me.ezzedine.mohammed.xpenser.utils.AccountUtils;
import me.ezzedine.mohammed.xpenser.utils.BudgetUtils;
import me.ezzedine.mohammed.xpenser.utils.TransactionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class AccountSummaryProjectionTest {

    private AccountSummaryProjection projection;
    private AccountSummaryStorage storage;

    @BeforeEach
    void setUp() {
        storage = mock(AccountSummaryStorage.class);
        projection = new AccountSummaryProjection(storage, new AccountSummaryProjectionMapperImpl());
        when(storage.save(any())).thenReturn(Mono.empty());
    }

    @Test
    @DisplayName("it should save an entity in the storage when a new account is opened")
    void it_should_save_an_entity_in_the_storage_when_a_new_account_is_opened() {
        projection.on(AccountUtils.accountOpenedEvent());

        verify(storage).save(AccountUtils.accountSummary().build());
    }

    @Test
    @DisplayName("it should update the amount of an account when a money deposited into account event is issued")
    void it_should_update_the_amount_of_an_account_when_a_money_deposited_into_account_event_is_issued() {
        when(storage.find(AccountUtils.ACCOUNT_ID)).thenReturn(Mono.just(AccountUtils.accountSummary().build()));

        projection.on(TransactionUtils.moneyDepositedIntoAccountEvent().build());

        verify(storage).save(AccountUtils.accountSummary().budget(BudgetUtils.budgetSummary().amount(BudgetUtils.BUDGET_AMOUNT.add(TransactionUtils.TRANSACTION_AMOUNT)).build()).build());
    }

    @Test
    @DisplayName("it should update the amount of an account when a money withdrew even is issued")
    void it_should_update_the_amount_of_an_account_when_a_money_withdrew_even_is_issued() {
        when(storage.find(AccountUtils.ACCOUNT_ID)).thenReturn(Mono.just(AccountUtils.accountSummary().build()));

        projection.on(TransactionUtils.moneyWithdrewFromAccountEvent().build());

        verify(storage).save(AccountUtils.accountSummary().budget(BudgetUtils.budgetSummary().amount(BudgetUtils.BUDGET_AMOUNT.subtract(TransactionUtils.TRANSACTION_AMOUNT)).build()).build());
    }

    @Test
    @DisplayName("it should fetch the existing account summaries from the store when handling fetch account summaries query")
    void it_should_fetch_the_existing_account_summaries_from_the_store_when_handling_fetch_account_summaries_query() {
        when(storage.fetchAll()).thenReturn(Flux.just(AccountUtils.accountSummary().build()));

        Flux<AccountSummary> accountSummaryFlux = projection.handle(new FetchAccountSummariesQuery());

        StepVerifier.create(accountSummaryFlux)
                .expectNext(AccountUtils.accountSummary().build())
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("it should fetch the account summary from the store when handling fetch account summary query")
    void it_should_fetch_the_account_summary_from_the_store_when_handling_fetch_account_summary_query() {
        when(storage.find(AccountUtils.ACCOUNT_ID)).thenReturn(Mono.just(AccountUtils.accountSummary().build()));

        Mono<AccountSummary> accountMono = projection.handle(new FetchAccountSummaryQuery(AccountUtils.ACCOUNT_ID));

        StepVerifier.create(accountMono)
                .expectNext(AccountUtils.accountSummary().build())
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("it should throw an error when the account id is not found in the store")
    void it_should_throw_an_error_when_the_account_id_is_not_found_in_the_store() {
        when(storage.find(AccountUtils.ACCOUNT_ID)).thenReturn(Mono.empty());

        Mono<AccountSummary> accountMono = projection.handle(new FetchAccountSummaryQuery(AccountUtils.ACCOUNT_ID));

        StepVerifier.create(accountMono)
                .expectError(AccountNotFoundException.class)
                .verify();
    }
}