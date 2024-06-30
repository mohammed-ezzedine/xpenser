package me.ezzedine.mohammed.xpenser.infra.persistence.expense.category.report;

import me.ezzedine.mohammed.xpenser.core.expense.category.report.ExpenseCategoryMonthlyReport;
import me.ezzedine.mohammed.xpenser.infra.config.persistence.MongoConfiguration;
import me.ezzedine.mohammed.xpenser.infra.persistence.DatabaseIntegrationTest;
import me.ezzedine.mohammed.xpenser.infra.persistence.conversion.yearmonth.StringToYearMonthConverter;
import me.ezzedine.mohammed.xpenser.infra.persistence.conversion.yearmonth.YearMonthToStringConverter;
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
    MongoConfiguration.class,
    MongoAutoConfiguration.class,
    ExpenseCategoryMonthlyReportMongoStorage.class,
    ExpenseCategoryMonthlyReportReactiveMongodbRepository.class,
    ExpenseCategoryMonthlyReportDocumentMapperImpl.class,
    StringToYearMonthConverter.class,
    YearMonthToStringConverter.class
})
class ExpenseCategoryMonthlyReportMongoStorageIntegrationTest extends DatabaseIntegrationTest {

    @Autowired
    private ExpenseCategoryMonthlyReportMongoStorage storage;

    @Autowired
    private ExpenseCategoryMonthlyReportReactiveMongodbRepository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAll().block();
    }

    @Nested
    @DisplayName("When fetching an entity by category and month")
    class FetchingByCategoryAndMonth {

        @Test
        @DisplayName("it should return the entity when it exists in the database")
        void it_should_return_the_entity_when_it_exists_in_the_database() {
            repository.save(ExpenseCategoryUtils.monthlyReportDocument().build()).block();

            Mono<ExpenseCategoryMonthlyReport> mono = storage.fetchByCategoryAndMonth(ExpenseCategoryUtils.EXPENSE_CATEGORY_ID, ExpenseCategoryUtils.REPORT_MONTH);
            StepVerifier.create(mono)
                    .expectNext(ExpenseCategoryUtils.monthlyReport().build())
                    .verifyComplete();
        }

        @Test
        @DisplayName("it should return an empty mono if only an entity with a different month exists")
        void it_should_return_an_empty_mono_if_only_an_entity_with_a_different_month_exists() {
            repository.save(ExpenseCategoryUtils.monthlyReportDocument().id(
                    ExpenseCategoryUtils.anotherMonthlyReportDocumentId().month(ExpenseCategoryUtils.ANOTHER_REPORT_MONTH).build()
            ).build()).block();

            Mono<ExpenseCategoryMonthlyReport> mono = storage.fetchByCategoryAndMonth(ExpenseCategoryUtils.EXPENSE_CATEGORY_ID, ExpenseCategoryUtils.REPORT_MONTH);
            StepVerifier.create(mono).verifyComplete();
        }

        @Test
        @DisplayName("it should return an empty mono if only an entity with a different category exists")
        void it_should_return_an_empty_mono_if_only_an_entity_with_a_different_category_exists() {
            repository.save(ExpenseCategoryUtils.monthlyReportDocument().id(
                    ExpenseCategoryUtils.anotherMonthlyReportDocumentId().category(ExpenseCategoryUtils.ANOTHER_EXPENSE_CATEGORY_ID).build()
            ).build()).block();

            Mono<ExpenseCategoryMonthlyReport> mono = storage.fetchByCategoryAndMonth(ExpenseCategoryUtils.EXPENSE_CATEGORY_ID, ExpenseCategoryUtils.REPORT_MONTH);
            StepVerifier.create(mono).verifyComplete();
        }
    }

    @Nested
    @DisplayName("When fetching the entities by month")
    class FetchingByMonth {

        @Test
        @DisplayName("it should return all the entities with a matching month")
        void it_should_return_all_the_entities_with_a_matching_month() {
            repository.save(ExpenseCategoryUtils.monthlyReportDocument().build()).block();
            repository.save(ExpenseCategoryUtils.anotherMonthlyReportDocument().build()).block();

            Flux<ExpenseCategoryMonthlyReport> flux = storage.fetchByMonth(ExpenseCategoryUtils.REPORT_MONTH);
            StepVerifier.create(flux)
                    .expectNext(ExpenseCategoryUtils.monthlyReport().build())
                    .verifyComplete();
        }

        @Test
        @DisplayName("it should return an empty result when no existing entity match the specified month")
        void it_should_return_an_empty_result_when_no_existing_entity_match_the_specified_month() {
            repository.save(ExpenseCategoryUtils.anotherMonthlyReportDocument().build()).block();

            Flux<ExpenseCategoryMonthlyReport> flux = storage.fetchByMonth(ExpenseCategoryUtils.REPORT_MONTH);
            StepVerifier.create(flux).verifyComplete();
        }
    }

    @Nested
    @DisplayName("When fetching the entities by category")
    class FetchingByCategory {

        @Test
        @DisplayName("it should return all the entities with a matching category")
        void it_should_return_all_the_entities_with_a_matching_category() {
            repository.save(ExpenseCategoryUtils.monthlyReportDocument().build()).block();
            repository.save(ExpenseCategoryUtils.anotherMonthlyReportDocument()
                    .id(ExpenseCategoryUtils.anotherMonthlyReportDocumentId()
                            .category(ExpenseCategoryUtils.EXPENSE_CATEGORY_ID)
                            .build())
                    .build()).block();

            Flux<ExpenseCategoryMonthlyReport> flux = storage.fetchByCategory(ExpenseCategoryUtils.EXPENSE_CATEGORY_ID);
            StepVerifier.create(flux)
                    .expectNext(ExpenseCategoryUtils.anotherMonthlyReport().category(ExpenseCategoryUtils.EXPENSE_CATEGORY_ID).build())
                    .expectNext(ExpenseCategoryUtils.monthlyReport().build())
                    .verifyComplete();
        }

        @Test
        @DisplayName("it should return an empty result when no existing entity match the specified category")
        void it_should_return_an_empty_result_when_no_existing_entity_match_the_specified_category() {
            repository.save(ExpenseCategoryUtils.anotherMonthlyReportDocument().build()).block();

            Flux<ExpenseCategoryMonthlyReport> flux = storage.fetchByCategory(ExpenseCategoryUtils.EXPENSE_CATEGORY_ID);
            StepVerifier.create(flux).verifyComplete();
        }
    }

    @Nested
    @DisplayName("When saving an entity in the database")
    class SavingEntity {

        @Test
        @DisplayName("it should persist the entity in the storage")
        void it_should_persist_the_entity_in_the_storage() {
            storage.save(ExpenseCategoryUtils.monthlyReport().build()).block();

            Flux<ExpenseCategoryMonthlyReportDocument> flux = repository.findAll();
            StepVerifier.create(flux)
                    .expectNext(ExpenseCategoryUtils.monthlyReportDocument().build())
                    .verifyComplete();
        }

        @Test
        @DisplayName("it should override any existing entity with the same id")
        void it_should_override_any_existing_entity_with_the_same_id() {
            repository.save(ExpenseCategoryUtils.anotherMonthlyReportDocument().id(ExpenseCategoryUtils.monthlyReportDocumentId().build()).build()).block();

            storage.save(ExpenseCategoryUtils.monthlyReport().build()).block();

            Flux<ExpenseCategoryMonthlyReportDocument> flux = repository.findAll();
            StepVerifier.create(flux)
                    .expectNext(ExpenseCategoryUtils.monthlyReportDocument().build())
                    .verifyComplete();
        }
    }
}