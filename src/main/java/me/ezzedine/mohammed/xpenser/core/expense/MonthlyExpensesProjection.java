package me.ezzedine.mohammed.xpenser.core.expense;

import lombok.RequiredArgsConstructor;
import me.ezzedine.mohammed.xpenser.core.account.transactions.MoneyDepositedInAccountEvent;
import me.ezzedine.mohammed.xpenser.core.account.transactions.MoneyTransferInitiatedEvent;
import me.ezzedine.mohammed.xpenser.core.account.transactions.MoneyWithdrewFromAccountEvent;
import me.ezzedine.mohammed.xpenser.core.account.transactions.transfer.ActiveTransfer;
import me.ezzedine.mohammed.xpenser.core.account.transactions.transfer.ActiveTransferStorage;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MonthlyExpensesProjection {

    private final ActiveTransferStorage activeTransferStorage;
    private final MonthlyReportStorage monthlyReportStorage;
    private final YearMonthFactory yearMonthFactory;

    @EventHandler
    public void on(MoneyTransferInitiatedEvent event) {
        activeTransferStorage.save(new ActiveTransfer(event.transactionId())).block();
    }

    @EventHandler
    public void on(MoneyDepositedInAccountEvent event) {
        activeTransferStorage.exists(event.transactionId())
            .flatMap(exists -> {
                if (exists) {
                    return activeTransferStorage.delete(event.transactionId());
                } else {
                    return monthlyReportStorage.fetch(yearMonthFactory.from(event.timestamp()))
                            .defaultIfEmpty(MonthlyReport.builder().month(yearMonthFactory.from(event.timestamp())).build())
                            .map(report -> report.addIncome(event.amount()))
                            .flatMap(monthlyReportStorage::save);
                }
            })
            .block();
    }

    @EventHandler
    public void on(MoneyWithdrewFromAccountEvent event) {
        activeTransferStorage.exists(event.transactionId())
            .filter(exists -> !exists)
            .flatMap(exists -> monthlyReportStorage.fetch(yearMonthFactory.from(event.timestamp()))
                    .defaultIfEmpty(MonthlyReport.builder().month(yearMonthFactory.from(event.timestamp())).build()))
            .map(report -> report.addExpense(event.amount()))
            .flatMap(monthlyReportStorage::save)
            .block();
    }
}
