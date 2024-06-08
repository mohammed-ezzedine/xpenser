package me.ezzedine.mohammed.xpenser.core.account.projection.summary;

import reactor.core.publisher.Mono;

public interface AccountSummaryStorage {
    Mono<Void> save(AccountSummary accountSummary);
    Mono<AccountSummary> find(String id);
}
