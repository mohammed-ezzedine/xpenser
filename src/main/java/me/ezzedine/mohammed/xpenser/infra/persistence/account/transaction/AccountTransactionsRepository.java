package me.ezzedine.mohammed.xpenser.infra.persistence.account.transaction;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface AccountTransactionsRepository extends ReactiveMongoRepository<AccountTransactionsDocument, String> {
}
