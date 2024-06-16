package me.ezzedine.mohammed.xpenser.infra.expense;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ExpenseCategoryUuidGeneratorTest {

    @Test
    @DisplayName("generates a uuid")
    void generates_a_uuid() {
        Mono<String> mono = new ExpenseCategoryUuidGenerator().generate();
        StepVerifier.create(mono)
                .assertNext(id -> assertDoesNotThrow(() -> UUID.fromString(id)))
                .expectComplete()
                .verify();
    }

}