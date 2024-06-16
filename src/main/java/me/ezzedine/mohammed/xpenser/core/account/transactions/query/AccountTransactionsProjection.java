package me.ezzedine.mohammed.xpenser.core.account.transactions.query;

import lombok.RequiredArgsConstructor;
import me.ezzedine.mohammed.xpenser.core.account.AccountNotFoundException;
import me.ezzedine.mohammed.xpenser.core.account.opening.AccountOpenedEvent;
import me.ezzedine.mohammed.xpenser.core.account.transactions.MoneyDepositedInAccountEvent;
import me.ezzedine.mohammed.xpenser.core.account.transactions.MoneyWithdrewFromAccountEvent;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountTransactionsProjection {

    private final AccountTransactionsStorage storage;
    private final AccountTransactionMapper mapper;

    @EventHandler
    public void on(AccountOpenedEvent event) {
        storage.save(AccountTransactionSummary.builder().id(event.id()).transactions(List.of(mapper.map(event))).build()).block();
    }

    @EventHandler
    public void on(MoneyDepositedInAccountEvent event) {
        storage.findById(event.accountId())
                .map(account -> {
                    ArrayList<TransactionSummary> updatedTransactions = new ArrayList<>();
                    TransactionSummary transactionSummary = TransactionSummary.builder()
                            .amount(event.amount()).note(event.note())
                            .balance(account.transactions().getFirst().balance().add(event.amount()))
                            .timestamp(event.timestamp()).build();

                    updatedTransactions.add(transactionSummary);
                    updatedTransactions.addAll(account.transactions());
                    return AccountTransactionSummary.builder().id(account.id()).transactions(updatedTransactions).build();
                })
                .flatMap(storage::save)
                .block();
    }

    @EventHandler
    public void on(MoneyWithdrewFromAccountEvent event) {
        storage.findById(event.accountId())
                .map(account -> {
                    ArrayList<TransactionSummary> updatedTransactions = new ArrayList<>();
                    TransactionSummary transactionSummary = TransactionSummary.builder()
                            .amount(event.amount().negate()).note(event.note()).category(event.category())
                            .balance(account.transactions().getFirst().balance().subtract(event.amount()))
                            .timestamp(event.timestamp()).build();
                    updatedTransactions.add(transactionSummary);
                    updatedTransactions.addAll(account.transactions());
                    return AccountTransactionSummary.builder().id(account.id()).transactions(updatedTransactions).build();
                })
                .flatMap(storage::save)
                .block();
    }

    @QueryHandler
    public Flux<TransactionSummary> handle(FetchAccountTransactionsQuery query) {
        return storage.findById(query.accountId())
                .flatMapIterable(AccountTransactionSummary::transactions)
                .switchIfEmpty(Mono.error(new AccountNotFoundException(query.accountId())));
    }
}
