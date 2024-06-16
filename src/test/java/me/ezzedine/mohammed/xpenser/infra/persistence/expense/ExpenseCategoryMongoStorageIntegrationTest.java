package me.ezzedine.mohammed.xpenser.infra.persistence.expense;

import me.ezzedine.mohammed.xpenser.core.expense.ExpenseCategory;
import me.ezzedine.mohammed.xpenser.infra.persistence.DatabaseIntegrationTest;
import me.ezzedine.mohammed.xpenser.utils.ExpenseCategoryUtils;
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

@SpringBootTest(classes = {
        MongoAutoConfiguration.class,
        ExpenseCategoryMongoStorage.class,
        ExpenseCategoryDocumentReactiveMonoRepository.class,
        ExpenseCategoryDocumentMapperImpl.class
})
class ExpenseCategoryMongoStorageIntegrationTest extends DatabaseIntegrationTest {

    @Autowired
    private ExpenseCategoryMongoStorage storage;

    @Autowired
    private ExpenseCategoryDocumentReactiveMonoRepository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAll().block();
    }

    @Nested
    @DisplayName("When saving an entity in the storage")
    class SavingEntity {

        @Test
        @DisplayName("it should become persisted and accessible from the database")
        void it_should_become_persisted_and_accessible_from_the_database() {
            storage.save(ExpenseCategoryUtils.expenseCategory().build()).block();

            Mono<ExpenseCategoryDocument> mono = repository.findById(ExpenseCategoryUtils.EXPENSE_CATEGORY_ID);
            StepVerifier.create(mono)
                    .expectNext(ExpenseCategoryUtils.expenseCategoryDocument().build())
                    .expectComplete()
                    .verify();
        }

        @Test
        @DisplayName("it should override any previous document existing in the database with the same id")
        void it_should_override_any_previous_document_existing_in_the_database_with_the_same_id() {
            repository.save(ExpenseCategoryUtils.anotherExpenseCategoryDocument().id(ExpenseCategoryUtils.EXPENSE_CATEGORY_ID).build()).block();

            storage.save(ExpenseCategoryUtils.expenseCategory().build()).block();

            Mono<ExpenseCategoryDocument> mono = repository.findById(ExpenseCategoryUtils.EXPENSE_CATEGORY_ID);
            StepVerifier.create(mono)
                    .expectNext(ExpenseCategoryUtils.expenseCategoryDocument().build())
                    .expectComplete()
                    .verify();
        }
    }

    @Nested
    @DisplayName("When fetching an entity from the database by id")
    class FetchingById {

        @Test
        @DisplayName("it should return an empty optional when no entity matching the id exists")
        void it_should_return_an_empty_optional_when_no_entity_matching_the_id_exists() {
            repository.save(ExpenseCategoryUtils.expenseCategoryDocument().build()).block();

            Mono<ExpenseCategory> mono = storage.fetch(ExpenseCategoryUtils.ANOTHER_EXPENSE_CATEGORY_ID);
            StepVerifier.create(mono)
                    .expectComplete()
                    .verify();
        }

        @Test
        @DisplayName("it should return the correct entity when it exists in the database")
        void it_should_return_the_correct_entity_when_it_exists_in_the_database() {
            repository.save(ExpenseCategoryUtils.expenseCategoryDocument().build()).block();
            repository.save(ExpenseCategoryUtils.anotherExpenseCategoryDocument().build()).block();

            Mono<ExpenseCategory> mono = storage.fetch(ExpenseCategoryUtils.EXPENSE_CATEGORY_ID);
            StepVerifier.create(mono)
                    .expectNext(ExpenseCategoryUtils.expenseCategory().build())
                    .expectComplete()
                    .verify();
        }
    }

    @Nested
    @DisplayName("When fetching the list of entities in the database")
    class FetchAll {
        @Test
        @DisplayName("it should return an empty list when non is found")
        void it_should_return_an_empty_list_when_non_is_found() {
            Flux<ExpenseCategory> flux = storage.fetchAll();

            StepVerifier.create(flux)
                    .expectComplete()
                    .verify();
        }

        @Test
        @DisplayName("it should return all the entities found in the database")
        void it_should_return_all_the_entities_found_in_the_database() {
            repository.save(ExpenseCategoryUtils.expenseCategoryDocument().build()).block();
            repository.save(ExpenseCategoryUtils.anotherExpenseCategoryDocument().build()).block();

            Flux<ExpenseCategory> flux = storage.fetchAll();
            StepVerifier.create(flux)
                    .expectNext(ExpenseCategoryUtils.expenseCategory().build())
                    .expectNext(ExpenseCategoryUtils.anotherExpenseCategory().build())
                    .expectComplete()
                    .verify();
        }
    }
}