package me.ezzedine.mohammed.xpenser.core.account.transactions.query;

import reactor.core.publisher.Mono;

public interface AccountTransactionsStorage {

    Mono<Void> save(AccountTransactionSummary summary);
    Mono<AccountTransactionSummary> fetch(String id);
}
