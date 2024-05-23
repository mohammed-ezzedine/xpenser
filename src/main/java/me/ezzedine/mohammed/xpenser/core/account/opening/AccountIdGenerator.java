package me.ezzedine.mohammed.xpenser.core.account.opening;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
public class AccountIdGenerator {

    public Mono<String> generate() {
        return Mono.just(UUID.randomUUID().toString())
                .doOnNext(id -> log.info("Account generated ID: {}", id));
    }
}
