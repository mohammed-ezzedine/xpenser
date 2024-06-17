package me.ezzedine.mohammed.xpenser.core.account.transactions.transfer;

import reactor.core.publisher.Mono;

public interface ActiveTransferStorage {

    Mono<Void> save(ActiveTransfer transfer);
    Mono<Boolean> exists(String transactionId);
    Mono<Void> delete(String transactionId);
}
