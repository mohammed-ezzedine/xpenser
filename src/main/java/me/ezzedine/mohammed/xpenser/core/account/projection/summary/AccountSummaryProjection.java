package me.ezzedine.mohammed.xpenser.core.account.projection.summary;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ezzedine.mohammed.xpenser.core.account.AccountNotFoundException;
import me.ezzedine.mohammed.xpenser.core.account.opening.AccountOpenedEvent;
import me.ezzedine.mohammed.xpenser.core.account.transactions.MoneyDepositedInAccountEvent;
import me.ezzedine.mohammed.xpenser.core.account.transactions.MoneyWithdrewFromAccountEvent;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountSummaryProjection {

    private final AccountSummaryStorage storage;
    private final AccountSummaryProjectionMapper mapper;

    @EventHandler
    public void on(AccountOpenedEvent event) {
        log.info("handling event {}", event);
        storage.save(mapper.map(event)).block();
    }

    @EventHandler
    public void on(MoneyDepositedInAccountEvent event) {
        log.info("handling event {}", event);
        storage.find(event.accountId())
                .map(account -> new AccountSummary(event.accountId(), account.name(), new BudgetSummary(account.budget().currencyCode(), account.budget().amount().add(event.amount()))))
                .flatMap(storage::save)
                .block();
    }

    @EventHandler
    public void on(MoneyWithdrewFromAccountEvent event) {
        log.info("handling event {}", event);
        storage.find(event.accountId())
                .map(account -> new AccountSummary(event.accountId(), account.name(), new BudgetSummary(account.budget().currencyCode(), account.budget().amount().subtract(event.amount()))))
                .flatMap(storage::save)
                .block();
    }

    @QueryHandler
    public Mono<AccountSummary> handle(FetchAccountSummaryQuery query) {
        return storage.find(query.acocuntId())
                .switchIfEmpty(Mono.error(new AccountNotFoundException(query.acocuntId())));
    }

    @QueryHandler
    public Flux<AccountSummary> handle(FetchAccountSummariesQuery query) {
        return storage.fetchAll();
    }
}
