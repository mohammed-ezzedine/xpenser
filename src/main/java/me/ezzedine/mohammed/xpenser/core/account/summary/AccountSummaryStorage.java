package me.ezzedine.mohammed.xpenser.core.account.summary;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountSummaryStorage {
    Mono<Void> save(AccountSummary accountSummary);
    Mono<AccountSummary> find(String id);
    Flux<AccountSummary> fetchAll();
}
