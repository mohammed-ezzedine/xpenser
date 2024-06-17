package me.ezzedine.mohammed.xpenser.infra.persistence.expense;

import me.ezzedine.mohammed.xpenser.core.expense.MonthlyReport;
import me.ezzedine.mohammed.xpenser.infra.config.persistence.MongoConfiguration;
import me.ezzedine.mohammed.xpenser.infra.persistence.DatabaseIntegrationTest;
import me.ezzedine.mohammed.xpenser.infra.persistence.conversion.yearmonth.StringToYearMonthConverter;
import me.ezzedine.mohammed.xpenser.infra.persistence.conversion.yearmonth.YearMonthToStringConverter;
import me.ezzedine.mohammed.xpenser.utils.MonthlyReportUtils;
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
    MongoAutoConfiguration.class,
    MonthlyReportDocumentReactiveMongoRepository.class,
    MonthlyReportMongoStorage.class,
    MonthlyReportDocumentMapperImpl.class,
    YearMonthToStringConverter.class,
    StringToYearMonthConverter.class,
    MongoConfiguration.class
})
class MonthlyReportMongoStorageIntegrationTest extends DatabaseIntegrationTest {

    @Autowired
    private MonthlyReportDocumentReactiveMongoRepository repository;

    @Autowired
    private MonthlyReportMongoStorage storage;

    @AfterEach
    void tearDown() {
        repository.deleteAll().block();
    }

    @Nested
    @DisplayName("When saving an entity")
    class SavingEntity {

        @Test
        @DisplayName("it should persist the entity in the database")
        void it_should_persist_the_entity_in_the_database() {
            storage.save(MonthlyReportUtils.monthlyReport().build()).block();

            Mono<MonthlyReportDocument> mono = repository.findById(MonthlyReportUtils.MONTH);
            StepVerifier.create(mono)
                .expectNext(MonthlyReportUtils.monthlyReportDocument().build())
                .verifyComplete();
        }

        @Test
        @DisplayName("it should override any existing entity with the same month")
        void it_should_override_any_existing_entity_with_the_same_month() {
            repository.save(MonthlyReportUtils.anotherMonthlyReportDocument().month(MonthlyReportUtils.MONTH).build()).block();

            storage.save(MonthlyReportUtils.monthlyReport().build()).block();

            Mono<MonthlyReportDocument> mono = repository.findById(MonthlyReportUtils.MONTH);
            StepVerifier.create(mono)
                    .expectNext(MonthlyReportUtils.monthlyReportDocument().build())
                    .verifyComplete();
        }
    }

    @Nested
    @DisplayName("When fetching a monthly report")
    class FetchingEntity {

        @Test
        @DisplayName("it should return an empty result when no entity exists for it")
        void it_should_return_an_empty_result_when_no_entity_exists_for_it() {
            Mono<MonthlyReport> mono = storage.fetch(MonthlyReportUtils.MONTH);
            StepVerifier.create(mono).verifyComplete();
        }

        @Test
        @DisplayName("it should return the entity when it exists")
        void it_should_return_the_entity_when_it_exists() {
            repository.save(MonthlyReportUtils.monthlyReportDocument().build()).block();

            Mono<MonthlyReport> mono = storage.fetch(MonthlyReportUtils.MONTH);
            StepVerifier.create(mono)
                    .expectNext(MonthlyReportUtils.monthlyReport().build())
                    .verifyComplete();
        }
    }
}