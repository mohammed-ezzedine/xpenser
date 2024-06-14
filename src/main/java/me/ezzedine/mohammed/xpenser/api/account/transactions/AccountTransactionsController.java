package me.ezzedine.mohammed.xpenser.api.account.transactions;

import lombok.RequiredArgsConstructor;
import me.ezzedine.mohammed.xpenser.core.account.transactions.*;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequestMapping("accounts/{id}/transactions/")
@RestController
@RequiredArgsConstructor
public class AccountTransactionsController {

    private final CommandGateway commandGateway;
    private final DateFactory dateFactory;
    private final TransactionIdGenerator transactionIdGenerator;

    @PostMapping("deposit")
    public Mono<Void> addMoneyToAccount(@PathVariable String id, @RequestBody AddMoneyToAccountApiRequest request) {
        return transactionIdGenerator.generate()
                .map(transactionId -> new DepositMoneyCommand(transactionId, id, request.amount(), request.note(), dateFactory.now()))
                .doOnNext(commandGateway::sendAndWait)
                .then();
    }

    @PostMapping("withdraw")
    public Mono<Void> withdrawMoneyFromAccount(@PathVariable String id, @RequestBody WithdrawMoneyToAccountApiRequest request) {
        return transactionIdGenerator.generate()
                .map(transactionId -> new WithdrawMoneyCommand(transactionId, id, request.amount(), request.note(), dateFactory.now()))
                .doOnNext(commandGateway::sendAndWait)
                .then();
    }

    @PostMapping("transfer")
    public Mono<Void> transferMoneyFromAccount(@PathVariable String id, @RequestBody TransferMoneyApiRequest request) {
        return transactionIdGenerator.generate()
                .map(transactionId -> new TransferMoneyCommand(id, request.destinationAccountId(), transactionId, request.amount(), dateFactory.now()))
                .doOnNext(commandGateway::sendAndWait)
                .then();
    }
}
