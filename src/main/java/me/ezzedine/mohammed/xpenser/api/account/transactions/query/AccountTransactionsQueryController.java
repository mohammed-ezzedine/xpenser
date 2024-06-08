package me.ezzedine.mohammed.xpenser.api.account.transactions.query;

import lombok.RequiredArgsConstructor;
import me.ezzedine.mohammed.xpenser.core.account.transactions.query.FetchAccountTransactionsQuery;
import me.ezzedine.mohammed.xpenser.core.account.transactions.query.TransactionSummary;
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("accounts/{id}/transactions")
@RequiredArgsConstructor
public class AccountTransactionsQueryController {

    private final ReactorQueryGateway queryGateway;

    @GetMapping
    public Flux<TransactionSummary> getAccountTransactions(@PathVariable String id) {
        return queryGateway.streamingQuery(new FetchAccountTransactionsQuery(id), TransactionSummary.class);
    }
}
