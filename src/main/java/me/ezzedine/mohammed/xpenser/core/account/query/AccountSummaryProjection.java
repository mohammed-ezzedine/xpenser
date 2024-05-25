package me.ezzedine.mohammed.xpenser.core.account.query;

import lombok.RequiredArgsConstructor;
import me.ezzedine.mohammed.xpenser.core.account.opening.AccountOpenedEvent;
import me.ezzedine.mohammed.xpenser.core.account.transactions.MoneyDepositedInAccountEvent;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AccountSummaryProjection {

    private final Map<String, AccountSummary> accountSummaries = new ConcurrentHashMap<>();
    private final QueryUpdateEmitter queryUpdateEmitter;

    @EventHandler
    public void on(AccountOpenedEvent event) {
        accountSummaries.put(event.id(), new AccountSummary(event.id(), event.name(), BudgetSummary.from(event.budget())));
        queryUpdateEmitter.emit(FetchAccountSummariesQuery.class, queryUpdateEmitter -> true, accountSummaries.values().stream().toList());
    }

    @EventHandler
    public void on(MoneyDepositedInAccountEvent event) {
        AccountSummary accountSummary = accountSummaries.get(event.accountId());
        accountSummaries.put(event.accountId(), new AccountSummary(event.accountId(), accountSummary.name(), new BudgetSummary(accountSummary.budget().currency(), accountSummary.budget().amount() + event.amount())));
        queryUpdateEmitter.emit(FetchAccountSummariesQuery.class, queryUpdateEmitter -> true, accountSummaries.values().stream().toList());
    }

    @QueryHandler
    public List<AccountSummary> handle(FetchAccountSummariesQuery query) {
        return accountSummaries.values().stream().toList();
    }
}
