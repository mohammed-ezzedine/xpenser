package me.ezzedine.mohammed.xpenser.infra.expense;

import me.ezzedine.mohammed.xpenser.core.expense.ExpenseCategoryIdGenerator;
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
