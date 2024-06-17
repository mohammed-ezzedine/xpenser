package me.ezzedine.mohammed.xpenser.core.expense;

import reactor.core.publisher.Mono;

import java.time.YearMonth;

public interface MonthlyReportStorage {
    Mono<Void> save(MonthlyReport report);
    Mono<MonthlyReport> fetch(YearMonth yearMonth);

}
