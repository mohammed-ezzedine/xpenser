package me.ezzedine.mohammed.xpenser.infra.persistence.account;

import me.ezzedine.mohammed.xpenser.core.account.projection.summary.AccountSummary;
import me.ezzedine.mohammed.xpenser.infra.persistence.DatabaseIntegrationTest;
import me.ezzedine.mohammed.xpenser.utils.AccountUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {
        AccountSummaryRepository.class,
        AccountSummaryMongoStorage.class,
        MongoAutoConfiguration.class,
        AccountSummaryDocumentMapperImpl.class
})
class AccountSummaryMongoStorageIntegrationTest extends DatabaseIntegrationTest {

    @Autowired
    private AccountSummaryRepository repository;
    @Autowired
    private AccountSummaryMongoStorage storage;

    @AfterEach
    void tearDown() {
        repository.deleteAll().block();
    }

    @Nested
    @DisplayName("When saving an entity in the database")
    class SavingEntity {

        @Test
        @DisplayName("the entity should be persisted in the database")
        void the_entity_should_be_persisted_in_the_database() {
            storage.save(AccountUtils.accountSummary().build()).block();

            Mono<AccountSummaryDocument> accountMono = repository.findById(AccountUtils.ACCOUNT_ID);

            StepVerifier
                    .create(accountMono)
                    .assertNext(account -> assertEquals(AccountUtils.accountSummaryDocument().build(), account))
                    .expectComplete()
                    .verify();
        }

        @Test
        @DisplayName("the entity should replace any previous entity persisted with the same id")
        void the_entity_should_replace_any_previous_entity_persisted_with_the_same_id() {
            repository.save(AccountUtils.anotherAccountSummaryDocument().id(AccountUtils.ACCOUNT_ID).build()).block();

            storage.save(AccountUtils.accountSummary().build()).block();

            Mono<AccountSummaryDocument> accountMono = repository.findById(AccountUtils.ACCOUNT_ID);
            StepVerifier.create(accountMono)
                    .assertNext(account -> assertEquals(AccountUtils.accountSummaryDocument().build(), account))
                    .expectComplete()
                    .verify();
        }
    }

    @Nested
    @DisplayName("When fetching an entity from the database")
    class FetchingEntity {

        @Test
        @DisplayName("it should return an empty optional if the id is not found")
        void it_should_return_an_empty_optional_if_the_id_is_not_found() {
            Mono<AccountSummary> accountSummaryMono = storage.find(UUID.randomUUID().toString());

            StepVerifier.create(accountSummaryMono)
                    .expectNextCount(0)
                    .expectComplete()
                    .verify();
        }

        @Test
        @DisplayName("it should return the entity when it is present")
        void it_should_return_the_entity_when_it_is_present() {
            repository.save(AccountUtils.accountSummaryDocument().build()).block();

            Mono<AccountSummary> accountSummaryMono = storage.find(AccountUtils.ACCOUNT_ID);

            StepVerifier.create(accountSummaryMono)
                    .assertNext(accountSummary ->  assertEquals(AccountUtils.accountSummary().build(), accountSummary))
                    .expectComplete()
                    .verify();
        }
    }

    @Nested
    @DisplayName("When fetching the list of all accounts")
    class FetchingAll {

        @Test
        @DisplayName("it should return an empty list when none exist")
        void it_should_return_an_empty_list_when_none_exist() {
            Flux<AccountSummary> accountSummaryFlux = storage.fetchAll();

            StepVerifier.create(accountSummaryFlux)
                    .expectNextCount(0)
                    .expectComplete()
                    .verify();
        }

        @Test
        @DisplayName("it should return the list of all accounts when some exist in the database")
        void it_should_return_the_list_of_all_accounts_when_some_exist_in_the_database() {
            repository.saveAll(List.of(
                    AccountUtils.accountSummaryDocument().build(),
                    AccountUtils.anotherAccountSummaryDocument().build())
            ).blockLast();

            Flux<AccountSummary> accountSummaryFlux = storage.fetchAll();
            StepVerifier.create(accountSummaryFlux)
                    .expectNext(AccountUtils.accountSummary().build(), AccountUtils.anotherAccountSummary().build())
                    .expectComplete()
                    .verify();
        }
    }
}