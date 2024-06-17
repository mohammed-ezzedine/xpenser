package me.ezzedine.mohammed.xpenser.core.expense.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ExpenseCategoryService {

    private final ExpenseCategoryStorage storage;
    private final ExpenseCategoryIdGenerator idGenerator;
    private final ExpenseCategoryMapper mapper;

    public Flux<ExpenseCategory> fetchAllCategories() {
        return storage.fetchAll();
    }

    public Mono<ExpenseCategory> create(CreateExpenseCategoryRequest request) {
        return idGenerator.generate()
                .map(id -> mapper.map(request, id))
                .flatMap(storage::save);
    }

    public Mono<ExpenseCategory> fetch(String id) {
        return storage.fetch(id);
    }
}
