package me.ezzedine.mohammed.xpenser.api.account.opening;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ezzedine.mohammed.xpenser.core.account.opening.AccountIdGenerator;
import me.ezzedine.mohammed.xpenser.core.account.opening.OpenAccountCommand;
import me.ezzedine.mohammed.xpenser.core.account.transactions.DateFactory;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@RestController
@RequestMapping("accounts/open")
@RequiredArgsConstructor
public class AccountOpeningController {

    private final CommandGateway commandGateway;
    private final AccountIdGenerator idGenerator;
    private final DateFactory dateFactory;

    @PostMapping
    public Mono<AccountIdentificationApiResponse> openAccount(@RequestBody OpenAccountApiRequest request) {
        log.info("Received a request to open a new account {}", request);

        return idGenerator.generate()
                .map(id -> OpenAccountCommand.builder()
                        .id(id)
                        .name(request.name())
                        .currencyCode(request.currency())
                        .budgetInitialAmount(request.initialAmount())
                        .timestamp(dateFactory.now())
                        .build())
                .doOnNext(commandGateway::send)
                .map(command -> new AccountIdentificationApiResponse(command.getId()))
                .onErrorStop()
                .timeout(Duration.ofSeconds(5));
    }
}
