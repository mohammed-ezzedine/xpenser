package me.ezzedine.mohammed.xpenser.infra.persistence.account.transaction.transfer;

import me.ezzedine.mohammed.xpenser.infra.persistence.DatabaseIntegrationTest;
import me.ezzedine.mohammed.xpenser.utils.TransactionUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest(classes = {
    ActiveTransferMongoStorage.class,
    ActiveTransferDocumentReactiveMongoRepository.class,
    MongoAutoConfiguration.class
})
class ActiveTransferMongoStorageIntegrationTest extends DatabaseIntegrationTest {

    @Autowired
    private ActiveTransferDocumentReactiveMongoRepository repository;

    @Autowired
    private ActiveTransferMongoStorage storage;

    @AfterEach
    void tearDown() {
        repository.deleteAll().block();
    }

    @Nested
    @DisplayName("When saving an entity")
    class SavingEntity {

        @Test
        @DisplayName("it should persist it in the database")
        void it_should_persist_it_in_the_database() {
            storage.save(TransactionUtils.activeTransfer().build()).block();

            Mono<ActiveTransferDocument> mono = repository.findById(TransactionUtils.TRANSACTION_ID);
            StepVerifier.create(mono)
                    .expectNext(TransactionUtils.activeTransferDocument().build())
                    .verifyComplete();
        }
    }

    @Nested
    @DisplayName("When checking if an entity exists")
    class CheckIfExists {
        @Test
        @DisplayName("it should return false if it does not")
        void it_should_return_false_if_it_does_not() {
            Mono<Boolean> mono = storage.exists(TransactionUtils.TRANSACTION_ID);
            StepVerifier.create(mono)
                    .expectNext(false)
                    .verifyComplete();
        }

        @Test
        @DisplayName("it should return true if it does")
        void it_should_return_true_if_it_does() {
            repository.save(TransactionUtils.activeTransferDocument().build()).block();

            Mono<Boolean> mono = storage.exists(TransactionUtils.TRANSACTION_ID);
            StepVerifier.create(mono)
                    .expectNext(true)
                    .verifyComplete();
        }
    }

    @Nested
    @DisplayName("When deleting an entity")
    class DeletingEntity {

        @Test
        @DisplayName("it should remove it from the database")
        void it_should_remove_it_from_the_database() {
            repository.save(TransactionUtils.activeTransferDocument().build()).block();

            storage.delete(TransactionUtils.TRANSACTION_ID).block();

            Mono<Boolean> mono = repository.existsById(TransactionUtils.TRANSACTION_ID);
            StepVerifier.create(mono)
                    .expectNext(false)
                    .verifyComplete();
        }
    }
}