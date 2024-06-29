package me.ezzedine.mohammed.xpenser.core.expense;

import lombok.RequiredArgsConstructor;
import me.ezzedine.mohammed.xpenser.core.account.RegularAccountAggregate;
import me.ezzedine.mohammed.xpenser.core.account.transactions.MoneyDepositedInAccountEvent;
import me.ezzedine.mohammed.xpenser.core.account.transactions.MoneyTransferInitiatedEvent;
import me.ezzedine.mohammed.xpenser.core.account.transactions.MoneyWithdrewFromAccountEvent;
import me.ezzedine.mohammed.xpenser.core.account.transactions.transfer.ActiveTransfer;
import me.ezzedine.mohammed.xpenser.core.account.transactions.transfer.ActiveTransferStorage;
import me.ezzedine.mohammed.xpenser.core.currency.CurrencyCode;
import me.ezzedine.mohammed.xpenser.core.currency.exchange.CurrencyExchangeManager;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.annotation.AggregateType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class MonthlyExpensesProjection {

    private final ActiveTransferStorage activeTransferStorage;
    private final MonthlyReportStorage monthlyReportStorage;
    private final YearMonthFactory yearMonthFactory;
    private final CurrencyExchangeManager currencyExchangeManager;
    private static final String targetAccountType = RegularAccountAggregate.class.getSimpleName();

    @EventHandler
    public void on(MoneyTransferInitiatedEvent event) {
        activeTransferStorage.save(new ActiveTransfer(event.transactionId())).block();
    }

    @EventHandler
    public void on(MoneyDepositedInAccountEvent event, @AggregateType String type) {
        activeTransferStorage.exists(event.transactionId())
            .flatMap(exists -> {
                if (exists) {
                    return activeTransferStorage.delete(event.transactionId());
                } else {

                    if (!targetAccountType.equals(type)) {
                        return Mono.empty();
                    }

                    return monthlyReportStorage.fetch(yearMonthFactory.from(event.timestamp()))
                            .defaultIfEmpty(MonthlyReport.builder().month(yearMonthFactory.from(event.timestamp())).target(BigDecimal.valueOf(1500)).build())
                            .map(report -> report.addIncome(currencyExchangeManager.convert(event.amount(), event.currency(), CurrencyCode.USD)))
                            .flatMap(monthlyReportStorage::save);
                }
            })
            .block();
    }

    @EventHandler
    public void on(MoneyWithdrewFromAccountEvent event, @AggregateType String type) {
        if (!targetAccountType.equals(type)) {
            return;
        }

        activeTransferStorage.exists(event.transactionId())
            .filter(exists -> !exists)
            .flatMap(exists -> monthlyReportStorage.fetch(yearMonthFactory.from(event.timestamp()))
                    .defaultIfEmpty(MonthlyReport.builder().month(yearMonthFactory.from(event.timestamp())).target(BigDecimal.valueOf(1500)).build()))
            .map(report -> report.addExpense(currencyExchangeManager.convert(event.amount(), event.currency(), CurrencyCode.USD)))
            .flatMap(monthlyReportStorage::save)
            .block();
    }
}
