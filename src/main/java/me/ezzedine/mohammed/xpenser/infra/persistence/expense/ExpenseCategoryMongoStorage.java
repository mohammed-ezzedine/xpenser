package me.ezzedine.mohammed.xpenser.infra.persistence.expense;

import lombok.RequiredArgsConstructor;
import me.ezzedine.mohammed.xpenser.core.expense.ExpenseCategory;
import me.ezzedine.mohammed.xpenser.core.expense.ExpenseCategoryStorage;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class ExpenseCategoryMongoStorage implements ExpenseCategoryStorage {

    private final ExpenseCategoryDocumentReactiveMonoRepository repository;
    private final ExpenseCategoryDocumentMapper mapper;

    @Override
    public Mono<ExpenseCategory> save(ExpenseCategory category) {
        return repository.save(mapper.map(category)).map(mapper::map);
    }

    @Override
    public Mono<ExpenseCategory> fetch(String id) {
        return repository.findById(id).map(mapper::map);
    }

    @Override
    public Flux<ExpenseCategory> fetchAll() {
        return repository.findAll().map(mapper::map);
    }
}
