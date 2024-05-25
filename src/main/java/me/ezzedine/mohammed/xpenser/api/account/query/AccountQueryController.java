package me.ezzedine.mohammed.xpenser.api.account.query;

import lombok.RequiredArgsConstructor;
import me.ezzedine.mohammed.xpenser.core.account.query.AccountSummary;
import me.ezzedine.mohammed.xpenser.core.account.query.FetchAccountSummariesQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping("accounts")
@RestController
@RequiredArgsConstructor
public class AccountQueryController {

    private final QueryGateway queryGateway;

    @GetMapping
    public Flux<AccountSummary> fetchAccountSummaries() {
        return Mono.fromFuture(queryGateway.query(new FetchAccountSummariesQuery(), ResponseTypes.multipleInstancesOf(AccountSummary.class)))
                .flatMapMany(i -> Flux.fromStream(i.stream()));
    }
}
