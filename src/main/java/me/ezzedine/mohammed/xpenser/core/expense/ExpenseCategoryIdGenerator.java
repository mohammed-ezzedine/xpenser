package me.ezzedine.mohammed.xpenser.core.expense;

import reactor.core.publisher.Mono;

public interface ExpenseCategoryIdGenerator {
    Mono<String> generate();
}
