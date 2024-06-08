package me.ezzedine.mohammed.xpenser.infra.persistence.account;

import lombok.RequiredArgsConstructor;
import me.ezzedine.mohammed.xpenser.core.account.projection.summary.AccountSummary;
import me.ezzedine.mohammed.xpenser.core.account.projection.summary.AccountSummaryStorage;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class AccountSummaryMongoStorage implements AccountSummaryStorage {

    private final AccountSummaryRepository repository;
    private final AccountSummaryDocumentMapper mapper;

    @Override
    public Mono<Void> save(AccountSummary accountSummary) {
        return repository.save(mapper.map(accountSummary)).then();
    }

    @Override
    public Mono<AccountSummary> find(String id) {
        return repository.findById(id).map(mapper::map);
    }
}
