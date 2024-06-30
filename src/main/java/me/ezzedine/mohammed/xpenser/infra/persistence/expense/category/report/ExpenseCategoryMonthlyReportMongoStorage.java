package me.ezzedine.mohammed.xpenser.infra.persistence.expense.category.report;

import lombok.RequiredArgsConstructor;
import me.ezzedine.mohammed.xpenser.core.expense.category.report.ExpenseCategoryMonthlyReport;
import me.ezzedine.mohammed.xpenser.core.expense.category.report.ExpenseCategoryMonthlyReportStorage;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.YearMonth;

@Repository
@RequiredArgsConstructor
public class ExpenseCategoryMonthlyReportMongoStorage implements ExpenseCategoryMonthlyReportStorage {

    private final ExpenseCategoryMonthlyReportReactiveMongodbRepository repository;
    private final ExpenseCategoryMonthlyReportDocumentMapper mapper;
    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<ExpenseCategoryMonthlyReport> fetchByCategoryAndMonth(String category, YearMonth month) {
        return repository.findById(ExpenseCategoryMonthlyReportDocument.CompositeKey.builder().category(category).month(month).build())
                .map(mapper::map);
    }

    @Override
    public Flux<ExpenseCategoryMonthlyReport> fetchByMonth(YearMonth month) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id.month").is(month));
        Flux<ExpenseCategoryMonthlyReportDocument> flux = mongoTemplate.find(query, ExpenseCategoryMonthlyReportDocument.class);
        return flux.map(mapper::map);
    }

    @Override
    public Flux<ExpenseCategoryMonthlyReport> fetchByCategory(String category) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id.category").is(category));
        query.with(Sort.by("id.month").ascending());
        Flux<ExpenseCategoryMonthlyReportDocument> flux = mongoTemplate.find(query, ExpenseCategoryMonthlyReportDocument.class);
        return flux.map(mapper::map);
    }

    @Override
    public Mono<Void> save(ExpenseCategoryMonthlyReport report) {
        return repository.save(mapper.map(report)).then();
    }
}
