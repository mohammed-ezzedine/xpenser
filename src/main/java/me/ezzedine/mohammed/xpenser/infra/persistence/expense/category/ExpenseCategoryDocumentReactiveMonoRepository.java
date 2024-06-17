package me.ezzedine.mohammed.xpenser.infra.persistence.expense.category;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ExpenseCategoryDocumentReactiveMonoRepository extends ReactiveMongoRepository<ExpenseCategoryDocument, String> {
}
