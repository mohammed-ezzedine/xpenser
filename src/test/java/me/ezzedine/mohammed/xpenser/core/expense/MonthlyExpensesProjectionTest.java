package me.ezzedine.mohammed.xpenser.core.expense;

import me.ezzedine.mohammed.xpenser.core.account.transactions.transfer.ActiveTransferStorage;
import me.ezzedine.mohammed.xpenser.utils.MonthlyReportUtils;
import me.ezzedine.mohammed.xpenser.utils.TransactionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.time.YearMonth;

import static org.mockito.Mockito.*;

class MonthlyExpensesProjectionTest {

    private ActiveTransferStorage activeTransferStorage;
    private MonthlyReportStorage monthlyReportStorage;
    private MonthlyExpensesProjection projection;
    private YearMonth currentMonth;

    @BeforeEach
    void setUp() {
        activeTransferStorage = mock(ActiveTransferStorage.class);
        monthlyReportStorage = mock(MonthlyReportStorage.class);
        YearMonthFactory yearMonthFactory = mock(YearMonthFactory.class);
        projection = new MonthlyExpensesProjection(activeTransferStorage, monthlyReportStorage, yearMonthFactory);

        currentMonth = mock(YearMonth.class);
        when(yearMonthFactory.now()).thenReturn(currentMonth);
    }

    @Test
    @DisplayName("it should track the active transfers when handling money transfer initiated event")
    void it_should_track_the_active_transfers_when_handling_money_transfer_initiated_event() {
        when(activeTransferStorage.save(any())).thenReturn(Mono.empty());

        projection.on(TransactionUtils.moneyTransferInitiatedEvent().build());

        verify(activeTransferStorage).save(TransactionUtils.activeTransfer().build());
    }

    @Test
    @DisplayName("it should track the incoming money when receiving a money deposited event if it were not associated with an active transfer")
    void it_should_track_the_incoming_money_when_receiving_a_money_deposited_event_if_it_were_not_associated_with_an_active_transfer() {
        when(activeTransferStorage.exists(TransactionUtils.TRANSACTION_ID)).thenReturn(Mono.just(false));
        when(monthlyReportStorage.fetch(currentMonth)).thenReturn(Mono.just(MonthlyReportUtils.monthlyReport().build()));
        when(monthlyReportStorage.save(any())).thenReturn(Mono.empty());

        projection.on(TransactionUtils.moneyDepositedIntoAccountEvent().build());

        MonthlyReport expectedReport = MonthlyReportUtils.monthlyReport()
                .incoming(MonthlyReportUtils.INCOMING.add(TransactionUtils.TRANSACTION_AMOUNT))
                .build();
        verify(monthlyReportStorage).save(expectedReport);
    }

    @Test
    @DisplayName("it should add a new report when an existing one does not already exist when a money deposited event is not associated with an active transfer")
    void it_should_add_a_new_report_when_an_existing_one_does_not_already_exist_when_a_money_deposited_event_is_not_associated_with_an_active_transfer() {
        when(activeTransferStorage.exists(TransactionUtils.TRANSACTION_ID)).thenReturn(Mono.just(false));
        when(monthlyReportStorage.fetch(currentMonth)).thenReturn(Mono.empty());
        when(monthlyReportStorage.save(any())).thenReturn(Mono.empty());

        projection.on(TransactionUtils.moneyDepositedIntoAccountEvent().build());

        MonthlyReport expectedReport = MonthlyReport.builder().month(currentMonth).incoming(TransactionUtils.TRANSACTION_AMOUNT).build();
        verify(monthlyReportStorage).save(expectedReport);
    }

    @Test
    @DisplayName("it should ignore the incoming money of a money deposited event that is associated with an active transfer")
    void it_should_ignore_the_incoming_money_of_a_money_deposited_event_that_is_associated_with_an_active_transfer() {
        when(activeTransferStorage.exists(TransactionUtils.TRANSACTION_ID)).thenReturn(Mono.just(true));
        when(activeTransferStorage.delete(TransactionUtils.TRANSACTION_ID)).thenReturn(Mono.empty());

        projection.on(TransactionUtils.moneyDepositedIntoAccountEvent().build());
        verifyNoInteractions(monthlyReportStorage);
    }

    @Test
    @DisplayName("it should delete the active transfer from the storage when receiving a money deposited event that is associated with an active transfer")
    void it_should_delete_the_active_transfer_from_the_storage_when_receiving_a_money_deposited_event_that_is_associated_with_an_active_transfer() {
        when(activeTransferStorage.exists(TransactionUtils.TRANSACTION_ID)).thenReturn(Mono.just(true));
        when(activeTransferStorage.delete(TransactionUtils.TRANSACTION_ID)).thenReturn(Mono.empty());

        projection.on(TransactionUtils.moneyDepositedIntoAccountEvent().build());

        verify(activeTransferStorage).delete(TransactionUtils.TRANSACTION_ID);
    }

    @Test
    @DisplayName("it should track the expenses when receiving a money withdrew event that is not associated with an active transfer")
    void it_should_track_the_expenses_when_receiving_a_money_withdrew_event_that_is_not_associated_with_an_active_transfer() {
        when(activeTransferStorage.exists(TransactionUtils.TRANSACTION_ID)).thenReturn(Mono.just(false));
        when(monthlyReportStorage.fetch(currentMonth)).thenReturn(Mono.just(MonthlyReportUtils.monthlyReport().build()));
        when(monthlyReportStorage.save(any())).thenReturn(Mono.empty());

        projection.on(TransactionUtils.moneyWithdrewFromAccountEvent().build());

        MonthlyReport expectedReport = MonthlyReportUtils.monthlyReport()
                .expenses(MonthlyReportUtils.EXPENSES.add(TransactionUtils.TRANSACTION_AMOUNT))
                .build();
        verify(monthlyReportStorage).save(expectedReport);
    }

    @Test
    @DisplayName("it should save a new report in the storage when an existing one does not already exist upon receiving a money withdrew event that is not associated with an active transfer")
    void it_should_save_a_new_report_in_the_storage_when_an_existing_one_does_not_already_exist_upon_receiving_a_money_withdrew_event_that_is_not_associated_with_an_active_transfer() {
        when(activeTransferStorage.exists(TransactionUtils.TRANSACTION_ID)).thenReturn(Mono.just(false));
        when(monthlyReportStorage.fetch(currentMonth)).thenReturn(Mono.empty());
        when(monthlyReportStorage.save(any())).thenReturn(Mono.empty());

        projection.on(TransactionUtils.moneyWithdrewFromAccountEvent().build());

        MonthlyReport expectedReport = MonthlyReport.builder().month(currentMonth).expenses(TransactionUtils.TRANSACTION_AMOUNT).build();
        verify(monthlyReportStorage).save(expectedReport);
    }

    @Test
    @DisplayName("it should ignore any money withdrew event that is associated with an active transfer")
    void it_should_ignore_any_money_withdrew_event_that_is_associated_with_an_active_transfer() {
        when(activeTransferStorage.exists(TransactionUtils.TRANSACTION_ID)).thenReturn(Mono.just(true));

        projection.on(TransactionUtils.moneyWithdrewFromAccountEvent().build());
        verifyNoInteractions(monthlyReportStorage);
    }
}