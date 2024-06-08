package me.ezzedine.mohammed.xpenser.api.account.query;

import lombok.RequiredArgsConstructor;
import me.ezzedine.mohammed.xpenser.core.account.projection.summary.AccountSummary;
import me.ezzedine.mohammed.xpenser.core.account.projection.summary.FetchAccountSummariesQuery;
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RequestMapping("accounts")
@RestController
@RequiredArgsConstructor
public class AccountQueryController {

    private final ReactorQueryGateway queryGateway;

    @GetMapping
    public Flux<AccountSummary> fetchAccountSummaries() {
        return queryGateway.streamingQuery(new FetchAccountSummariesQuery(), AccountSummary.class);
    }
}
