package me.ezzedine.mohammed.xpenser.infra.persistence.account;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountSummaryRepository extends ReactiveMongoRepository<AccountSummaryDocument, String> {
}
