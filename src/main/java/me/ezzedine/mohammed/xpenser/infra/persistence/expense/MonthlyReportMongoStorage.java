package me.ezzedine.mohammed.xpenser.infra.persistence.expense;

import lombok.RequiredArgsConstructor;
import me.ezzedine.mohammed.xpenser.core.expense.MonthlyReport;
import me.ezzedine.mohammed.xpenser.core.expense.MonthlyReportStorage;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.YearMonth;

@Repository
@RequiredArgsConstructor
public class MonthlyReportMongoStorage implements MonthlyReportStorage {

    private final MonthlyReportDocumentReactiveMongoRepository repository;
    private final MonthlyReportDocumentMapper mapper;

    @Override
    public Mono<Void> save(MonthlyReport report) {
        return repository.save(mapper.map(report)).then();
    }

    @Override
    public Mono<MonthlyReport> fetch(YearMonth yearMonth) {
        return repository.findById(yearMonth).map(mapper::map);
    }

    @Override
    public Mono<MonthlyReport> fetchFirstMonthReport() {
        return repository.findAll(Sort.by("month").ascending())
                .map(mapper::map)
                .take(1)
                .singleOrEmpty();
    }

    @Override
    public Mono<MonthlyReport> fetchLastMonthReport() {
        return repository.findAll(Sort.by("month").descending())
                .map(mapper::map)
                .take(1)
                .singleOrEmpty();
    }
}
