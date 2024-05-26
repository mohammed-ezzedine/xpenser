package me.ezzedine.mohammed.xpenser.core.account.transactions;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class TransactionIdGenerator {

    public Mono<String> generate() {
        return Mono.just(UUID.randomUUID().toString());
    }
}
