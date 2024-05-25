package me.ezzedine.mohammed.xpenser.api.account.transactions;

import lombok.RequiredArgsConstructor;
import me.ezzedine.mohammed.xpenser.core.account.transactions.DepositMoneyCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequestMapping("accounts/{id}/transactions/")
@RestController
@RequiredArgsConstructor
public class AccountTransactionsController {

    private final CommandGateway commandGateway;

    @PostMapping("deposit")
    public Mono<Void> addMoneyToAccount(@PathVariable String id, @RequestBody AddMoneyToAccountApiRequest request) {
        return Mono.fromFuture(commandGateway.send(new DepositMoneyCommand(id, request.amount())));
    }
}
