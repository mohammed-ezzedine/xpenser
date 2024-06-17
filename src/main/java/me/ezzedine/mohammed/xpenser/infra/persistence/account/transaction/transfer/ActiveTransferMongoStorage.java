package me.ezzedine.mohammed.xpenser.infra.persistence.account.transaction.transfer;

import lombok.RequiredArgsConstructor;
import me.ezzedine.mohammed.xpenser.core.account.transactions.transfer.ActiveTransfer;
import me.ezzedine.mohammed.xpenser.core.account.transactions.transfer.ActiveTransferStorage;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class ActiveTransferMongoStorage implements ActiveTransferStorage {

    private final ActiveTransferDocumentReactiveMongoRepository repository;

    @Override
    public Mono<Void> save(ActiveTransfer transfer) {
        return repository.save(new ActiveTransferDocument(transfer.transactionId())).then();
    }

    @Override
    public Mono<Boolean> exists(String transactionId) {
        return repository.existsById(transactionId);
    }

    @Override
    public Mono<Void> delete(String transactionId) {
        return repository.deleteById(transactionId);
    }
}
