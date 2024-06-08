package me.ezzedine.mohammed.xpenser.infra.persistence.account.transaction;

import lombok.RequiredArgsConstructor;
import me.ezzedine.mohammed.xpenser.core.account.transactions.query.AccountTransactionSummary;
import me.ezzedine.mohammed.xpenser.core.account.transactions.query.AccountTransactionsStorage;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class AccountTransactionsMongoStorage implements AccountTransactionsStorage {

    private final AccountTransactionsRepository repository;
    private final AccountTransactionsMapper mapper;

    @Override
    public Mono<Void> save(AccountTransactionSummary summary) {
        return repository.save(mapper.map(summary)).then();
    }

    @Override
    public Mono<AccountTransactionSummary> findById(String id) {
        return repository.findById(id).map(mapper::map);
    }
}
