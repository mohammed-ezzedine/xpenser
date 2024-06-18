package me.ezzedine.mohammed.xpenser.api.account.transactions;

import lombok.RequiredArgsConstructor;
import me.ezzedine.mohammed.xpenser.core.account.transactions.*;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Optional;

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
                .map(transactionId -> DepositMoneyCommand.builder()
                        .transactionId(transactionId)
                        .accountId(id)
                        .amount(request.amount())
                        .note(request.note())
                        .timestamp(Optional.ofNullable(request.timestamp()).orElse(dateFactory.now()))
                        .build())
                .doOnNext(commandGateway::sendAndWait)
                .then();
    }

    @PostMapping("withdraw")
    public Mono<Void> withdrawMoneyFromAccount(@PathVariable String id, @RequestBody WithdrawMoneyToAccountApiRequest request) {
        return transactionIdGenerator.generate()
                .map(transactionId -> WithdrawMoneyCommand.builder()
                        .transactionId(transactionId)
                        .accountId(id)
                        .amount(request.amount())
                        .note(request.note())
                        .timestamp(Optional.ofNullable(request.timestamp()).orElse(dateFactory.now()))
                        .category(request.category())
                        .build())
                .doOnNext(commandGateway::sendAndWait)
                .then();
    }

    @PostMapping("transfer")
    public Mono<Void> transferMoneyFromAccount(@PathVariable String id, @RequestBody TransferMoneyApiRequest request) {
        return transactionIdGenerator.generate()
                .map(transactionId -> TransferMoneyCommand.builder()
                        .sourceAccountId(id)
                        .destinationAccountId(request.destinationAccountId())
                        .transactionId(transactionId)
                        .amount(request.amount())
                        .timestamp(Optional.ofNullable(request.timestamp()).orElse(dateFactory.now()))
                        .build())
                .doOnNext(commandGateway::sendAndWait)
                .then();
    }
}
