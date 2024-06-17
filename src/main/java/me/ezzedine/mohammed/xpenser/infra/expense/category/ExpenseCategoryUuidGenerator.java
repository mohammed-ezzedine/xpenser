package me.ezzedine.mohammed.xpenser.infra.expense.category;

import me.ezzedine.mohammed.xpenser.core.expense.category.ExpenseCategoryIdGenerator;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class ExpenseCategoryUuidGenerator implements ExpenseCategoryIdGenerator {
    @Override
    public Mono<String> generate() {
        return Mono.just(UUID.randomUUID().toString());
    }
}
