package me.ezzedine.mohammed.xpenser.core.expense.category.report;

import lombok.RequiredArgsConstructor;
import me.ezzedine.mohammed.xpenser.core.account.RegularAccountAggregate;
import me.ezzedine.mohammed.xpenser.core.account.transactions.MoneyWithdrewFromAccountEvent;
import me.ezzedine.mohammed.xpenser.core.currency.CurrencyCode;
import me.ezzedine.mohammed.xpenser.core.currency.exchange.CurrencyExchangeManager;
import me.ezzedine.mohammed.xpenser.core.expense.YearMonthFactory;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.annotation.AggregateType;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class MonthlyCategoryExpensesProjection {
    private static final String targetAccountType = RegularAccountAggregate.class.getSimpleName();

    private final ExpenseCategoryMonthlyReportStorage storage;
    private final YearMonthFactory yearMonthFactory;
    private final CurrencyExchangeManager currencyExchangeManager;

    @EventHandler
    public void handle(MoneyWithdrewFromAccountEvent event, @AggregateType String type) {
        if (event.category() == null || !targetAccountType.equals(type)) {
            return;
        }

        storage.fetchByCategoryAndMonth(event.category(), yearMonthFactory.from(event.timestamp()))
                .defaultIfEmpty(ExpenseCategoryMonthlyReport.builder().month(yearMonthFactory.from(event.timestamp())).category(event.category()).amount(BigDecimal.ZERO).build())
                .map(report -> report.toBuilder().amount(report.amount().add(currencyExchangeManager.convert(event.amount(), event.currency(), CurrencyCode.USD))).build())
                .flatMap(storage::save)
                .block();
    }

    @QueryHandler
    public Flux<ExpenseCategoryMonthlyReport> handle(FetchMonthlyExpensesReportByCategoriesQuery query) {
        return storage.fetchByMonth(query.month());
    }

    @QueryHandler
    public Flux<ExpenseCategoryMonthlyReport> handle(FetchMonthlyCategoryExpensesReportByQuery query) {
        return storage.fetchByCategory(query.category());
    }
}
