package me.ezzedine.mohammed.xpenser.api.account.opening;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ezzedine.mohammed.xpenser.core.account.opening.*;
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
                        .name(request.getName())
                        .currencyCode(request.getCurrency())
                        .budgetInitialAmount(request.getInitialAmount())
                        .timestamp(dateFactory.now())
                        .build())
                .doOnNext(commandGateway::sendAndWait)
                .map(command -> new AccountIdentificationApiResponse(command.id()))
                .onErrorStop()
                .timeout(Duration.ofSeconds(5));
    }

    @PostMapping("investments")
    public Mono<AccountIdentificationApiResponse> openInvestmentsAccount(@RequestBody OpenInvestmentsAccountApiRequest request) {
        log.info("Received a request to open a new investments account {}", request);

        return idGenerator.generate()
                .map(id -> OpenInvestmentsAccountCommand.builder()
                        .id(id)
                        .name(request.getName())
                        .currencyCode(request.getCurrency())
                        .budgetInitialAmount(request.getInitialAmount())
                        .timestamp(dateFactory.now())
                        .build())
                .doOnNext(commandGateway::sendAndWait)
                .map(command -> new AccountIdentificationApiResponse(command.id()))
                .onErrorStop()
                .timeout(Duration.ofSeconds(5));
    }

    @PostMapping("loan")
    public Mono<AccountIdentificationApiResponse> openLoanAccount(@RequestBody OpenLoanAccountApiRequest request) {
        log.info("Received a request to open a new loan account {}", request);

        return idGenerator.generate()
                .map(id -> OpenLoanAccountCommand.builder()
                        .id(id)
                        .name(request.getName())
                        .currencyCode(request.getCurrency())
                        .budgetInitialAmount(request.getInitialAmount())
                        .timestamp(dateFactory.now())
                        .build())
                .doOnNext(commandGateway::sendAndWait)
                .map(command -> new AccountIdentificationApiResponse(command.id()))
                .onErrorStop()
                .timeout(Duration.ofSeconds(5));
    }

    @PostMapping("regular")
    public Mono<AccountIdentificationApiResponse> openRegularAccount(@RequestBody OpenRegularAccountApiRequest request) {
        log.info("Received a request to open a new regular account {}", request);

        return idGenerator.generate()
                .map(id -> OpenRegularAccountCommand.builder()
                        .id(id)
                        .name(request.getName())
                        .currencyCode(request.getCurrency())
                        .budgetInitialAmount(request.getInitialAmount())
                        .timestamp(dateFactory.now())
                        .build())
                .doOnNext(commandGateway::sendAndWait)
                .map(command -> new AccountIdentificationApiResponse(command.id()))
                .onErrorStop()
                .timeout(Duration.ofSeconds(5));
    }

    @PostMapping("savings")
    public Mono<AccountIdentificationApiResponse> openSavingsAccount(@RequestBody OpenSavingsAccountApiRequest request) {
        log.info("Received a request to open a new savings account {}", request);

        return idGenerator.generate()
                .map(id -> OpenSavingsAccountCommand.builder()
                        .id(id)
                        .name(request.getName())
                        .currencyCode(request.getCurrency())
                        .budgetInitialAmount(request.getInitialAmount())
                        .timestamp(dateFactory.now())
                        .build())
                .doOnNext(commandGateway::sendAndWait)
                .map(command -> new AccountIdentificationApiResponse(command.id()))
                .onErrorStop()
                .timeout(Duration.ofSeconds(5));
    }
}
