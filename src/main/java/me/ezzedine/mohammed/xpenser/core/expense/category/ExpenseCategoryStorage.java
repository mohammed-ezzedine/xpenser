package me.ezzedine.mohammed.xpenser.core.expense.category;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ExpenseCategoryStorage {

    Mono<ExpenseCategory> save(ExpenseCategory category);
    Mono<ExpenseCategory> fetch(String id);
    Flux<ExpenseCategory> fetchAll();
}
