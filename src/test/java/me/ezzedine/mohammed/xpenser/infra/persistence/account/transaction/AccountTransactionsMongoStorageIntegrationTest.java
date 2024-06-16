package me.ezzedine.mohammed.xpenser.infra.persistence.account.transaction;

import me.ezzedine.mohammed.xpenser.core.account.transactions.query.AccountTransactionSummary;
import me.ezzedine.mohammed.xpenser.infra.persistence.DatabaseIntegrationTest;
import me.ezzedine.mohammed.xpenser.utils.AccountUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

@SpringBootTest(classes = {
        MongoAutoConfiguration.class,
        AccountTransactionsRepository.class,
        AccountTransactionsMongoStorage.class,
        AccountTransactionsMapperImpl.class
})
class AccountTransactionsMongoStorageIntegrationTest extends DatabaseIntegrationTest {

    @Autowired
    private AccountTransactionsRepository repository;
    @Autowired
    private AccountTransactionsMongoStorage storage;

    @AfterEach
    void tearDown() {
        repository.deleteAll().block();
    }

    @Nested
    @DisplayName("When saving the transactions of an account")
    class SavingAccountTransactions {

        @Test
        @DisplayName("the entity should be persisted in the database")
        void the_entity_should_be_persisted_in_the_database() {
            storage.save(AccountUtils.accountTransactionsSummary().build()).block();

            Mono<AccountTransactionsDocument> accountTransactionsDocumentMono = repository.findById(AccountUtils.ACCOUNT_ID);

            StepVerifier.create(accountTransactionsDocumentMono)
                    .expectNext(AccountUtils.accountTransactionsDocument().build())
                    .expectComplete()
                    .verify();
        }

        @Test
        @DisplayName("it should override the details of an existing entity with the same id")
        void it_should_override_the_details_of_an_existing_entity_with_the_same_id() {
            repository.save(AccountUtils.anotherAccountTransactionsDocument().id(AccountUtils.ACCOUNT_ID).build()).block();

            storage.save(AccountUtils.accountTransactionsSummary().build()).block();

            Mono<AccountTransactionsDocument> accountTransactionsDocumentMono = repository.findById(AccountUtils.ACCOUNT_ID);

            StepVerifier.create(accountTransactionsDocumentMono)
                    .expectNext(AccountUtils.accountTransactionsDocument().build())
                    .expectComplete()
                    .verify();
        }
    }

    @Nested
    @DisplayName("When fetching the account transaction entity from the database")
    class FetchingEntity {

        @Test
        @DisplayName("it should return an empty result when none exist")
        void it_should_return_an_empty_result_when_none_exist() {
            Mono<AccountTransactionSummary> mono = storage.findById(UUID.randomUUID().toString());

            StepVerifier.create(mono)
                    .expectNextCount(0)
                    .expectComplete()
                    .verify();
        }

        @Test
        @DisplayName("it should return the entity when it exists")
        void it_should_return_the_entity_when_it_exists() {
            repository.save(AccountUtils.accountTransactionsDocument().build()).block();

            Mono<AccountTransactionSummary> mono = storage.findById(AccountUtils.ACCOUNT_ID);

            StepVerifier.create(mono)
                    .expectNext(AccountUtils.accountTransactionsSummary().build())
                    .expectComplete()
                    .verify();
        }
    }
}