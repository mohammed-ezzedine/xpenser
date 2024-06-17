package me.ezzedine.mohammed.xpenser.api.account.query;

import lombok.RequiredArgsConstructor;
import me.ezzedine.mohammed.xpenser.core.account.summary.AccountSummary;
import me.ezzedine.mohammed.xpenser.core.account.summary.FetchAccountSummariesQuery;
import me.ezzedine.mohammed.xpenser.core.account.summary.FetchAccountSummaryQuery;
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping("accounts")
@RestController
@RequiredArgsConstructor
public class AccountQueryController {

    private final ReactorQueryGateway reactorQueryGateway;

    @GetMapping
    public Flux<AccountSummary> fetchAccountSummaries() {
        return reactorQueryGateway.streamingQuery(new FetchAccountSummariesQuery(), AccountSummary.class);
    }

    @GetMapping("{id}")
    public Mono<AccountSummary> fetchAccountSummary(@PathVariable String id) {
        return reactorQueryGateway.streamingQuery(new FetchAccountSummaryQuery(id), AccountSummary.class)
                .singleOrEmpty();
    }
}
