package me.ezzedine.mohammed.xpenser.infra.persistence.account.transaction.transfer;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ActiveTransferDocumentReactiveMongoRepository extends ReactiveMongoRepository<ActiveTransferDocument, String> {
}
