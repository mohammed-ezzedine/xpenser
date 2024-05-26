package me.ezzedine.mohammed.xpenser.core.account.transactions.query;

import lombok.RequiredArgsConstructor;
import me.ezzedine.mohammed.xpenser.core.account.opening.AccountOpenedEvent;
import me.ezzedine.mohammed.xpenser.core.account.transactions.MoneyDepositedInAccountEvent;
import me.ezzedine.mohammed.xpenser.core.account.transactions.MoneyWithdrewFromAccountEvent;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AccountTransactionsProjection {

    private final Map<String, List<TransactionSummary>> accountsTransactions = new ConcurrentHashMap<>();

    @EventHandler
    public void on(AccountOpenedEvent event) {
        accountsTransactions.put(event.id(), List.of(new TransactionSummary(event.budget().getAmount(), event.budget().getAmount(), "Account opening", event.timestamp())));
    }

    @EventHandler
    public void on(MoneyDepositedInAccountEvent event) {
        List<TransactionSummary> transactionSummaries = accountsTransactions.get(event.accountId());
        ArrayList<TransactionSummary> updatedTransactions = new ArrayList<>();
        updatedTransactions.add(new TransactionSummary(event.amount(), transactionSummaries.getFirst().balance() + event.amount(), event.note(), event.timestamp()));
        updatedTransactions.addAll(transactionSummaries);
        accountsTransactions.put(event.accountId(), updatedTransactions);
    }

    @EventHandler
    public void on(MoneyWithdrewFromAccountEvent event) {
        List<TransactionSummary> transactionSummaries = accountsTransactions.get(event.accountId());
        ArrayList<TransactionSummary> updatedTransactions = new ArrayList<>();
        updatedTransactions.add(new TransactionSummary(event.amount(), transactionSummaries.getFirst().balance() - event.amount(), event.note(), event.timestamp()));
        updatedTransactions.addAll(transactionSummaries);
        accountsTransactions.put(event.accountId(), updatedTransactions);
    }

    @QueryHandler
    public List<TransactionSummary> handle(FetchAccountTransactionsQuery query) {
        return accountsTransactions.get(query.accountId());
    }
}
