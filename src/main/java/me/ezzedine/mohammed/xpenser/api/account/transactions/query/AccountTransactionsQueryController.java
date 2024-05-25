package me.ezzedine.mohammed.xpenser.api.account.transactions.query;

import lombok.RequiredArgsConstructor;
import me.ezzedine.mohammed.xpenser.core.account.transactions.query.FetchAccountTransactionsQuery;
import me.ezzedine.mohammed.xpenser.core.account.transactions.query.TransactionSummary;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("accounts/{id}/transactions")
@RequiredArgsConstructor
public class AccountTransactionsQueryController {

    private final QueryGateway queryGateway;

    @GetMapping
    public Flux<TransactionSummary> getAccountTransactions(@PathVariable String id) {
        return Mono.fromFuture(queryGateway.query(new FetchAccountTransactionsQuery(id), ResponseTypes.multipleInstancesOf(TransactionSummary.class)))
                .flatMapMany(t -> Flux.fromStream(t.stream()));
    }
}
