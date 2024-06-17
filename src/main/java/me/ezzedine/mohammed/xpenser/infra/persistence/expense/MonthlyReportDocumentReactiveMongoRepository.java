package me.ezzedine.mohammed.xpenser.infra.persistence.expense;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import java.time.YearMonth;

public interface MonthlyReportDocumentReactiveMongoRepository extends ReactiveMongoRepository<MonthlyReportDocument, YearMonth> {
}
