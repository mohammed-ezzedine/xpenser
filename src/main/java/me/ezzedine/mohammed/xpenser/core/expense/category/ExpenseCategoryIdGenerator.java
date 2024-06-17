package me.ezzedine.mohammed.xpenser.core.expense.category;

import reactor.core.publisher.Mono;

public interface ExpenseCategoryIdGenerator {
    Mono<String> generate();
}
