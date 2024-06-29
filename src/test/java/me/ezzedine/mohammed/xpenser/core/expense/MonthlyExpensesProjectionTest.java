package me.ezzedine.mohammed.xpenser.core.expense;

import me.ezzedine.mohammed.xpenser.core.account.transactions.transfer.ActiveTransferStorage;
import me.ezzedine.mohammed.xpenser.core.currency.CurrencyCode;
import me.ezzedine.mohammed.xpenser.core.currency.exchange.CurrencyExchangeManager;
import me.ezzedine.mohammed.xpenser.utils.CurrencyUtils;
import me.ezzedine.mohammed.xpenser.utils.MonthlyReportUtils;
import me.ezzedine.mohammed.xpenser.utils.TransactionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.time.YearMonth;
import java.util.UUID;

import static org.mockito.Mockito.*;

class MonthlyExpensesProjectionTest {

    public static final String REGULAR_ACCOUNT_AGGREGATE_TYPE = "RegularAccountAggregate";
    private ActiveTransferStorage activeTransferStorage;
    private MonthlyReportStorage monthlyReportStorage;
    private MonthlyExpensesProjection projection;
    private YearMonth transactionMonth;
    private CurrencyExchangeManager currencyExchangeManager;

    @BeforeEach
    void setUp() {
        activeTransferStorage = mock(ActiveTransferStorage.class);
        monthlyReportStorage = mock(MonthlyReportStorage.class);
        YearMonthFactory yearMonthFactory = mock(YearMonthFactory.class);
        currencyExchangeManager = mock(CurrencyExchangeManager.class);
        projection = new MonthlyExpensesProjection(activeTransferStorage, monthlyReportStorage, yearMonthFactory, currencyExchangeManager);

        transactionMonth = mock(YearMonth.class);
        when(yearMonthFactory.from(TransactionUtils.TRANSACTION_DATE)).thenReturn(transactionMonth);
        when(currencyExchangeManager.convert(TransactionUtils.TRANSACTION_AMOUNT, CurrencyUtils.currencyCode(), CurrencyCode.USD))
                .thenReturn(TransactionUtils.ANOTHER_TRANSACTION_AMOUNT);
    }

    @Test
    @DisplayName("it should track the active transfers when handling money transfer initiated event")
    void it_should_track_the_active_transfers_when_handling_money_transfer_initiated_event() {
        when(activeTransferStorage.save(any())).thenReturn(Mono.empty());

        projection.on(TransactionUtils.moneyTransferInitiatedEvent().build());

        verify(activeTransferStorage).save(TransactionUtils.activeTransfer().build());
    }

    @Test
    @DisplayName("it should not track the incoming money when receiving a money deposited event to an account that is not a regular one")
    void it_should_not_track_the_incoming_money_when_receiving_a_money_deposited_event_to_an_account_that_is_not_a_regular_one() {
        when(activeTransferStorage.exists(TransactionUtils.TRANSACTION_ID)).thenReturn(Mono.just(false));
        when(activeTransferStorage.delete(TransactionUtils.TRANSACTION_ID)).thenReturn(Mono.empty());

        projection.on(TransactionUtils.moneyDepositedIntoAccountEvent().build(), UUID.randomUUID().toString());
        verifyNoInteractions(monthlyReportStorage);
    }

    @Test
    @DisplayName("it should track the incoming money when receiving a money deposited event if it were not associated with an active transfer")
    void it_should_track_the_incoming_money_when_receiving_a_money_deposited_event_if_it_were_not_associated_with_an_active_transfer() {
        when(activeTransferStorage.exists(TransactionUtils.TRANSACTION_ID)).thenReturn(Mono.just(false));
        when(monthlyReportStorage.fetch(transactionMonth)).thenReturn(Mono.just(MonthlyReportUtils.monthlyReport().build()));
        when(monthlyReportStorage.save(any())).thenReturn(Mono.empty());

        projection.on(TransactionUtils.moneyDepositedIntoAccountEvent().build(), REGULAR_ACCOUNT_AGGREGATE_TYPE);

        MonthlyReport expectedReport = MonthlyReportUtils.monthlyReport()
                .incoming(MonthlyReportUtils.INCOMING.add(TransactionUtils.ANOTHER_TRANSACTION_AMOUNT))
                .build();
        verify(monthlyReportStorage).save(expectedReport);
    }

    @Test
    @DisplayName("it should add a new report when an existing one does not already exist when a money deposited event is not associated with an active transfer")
    void it_should_add_a_new_report_when_an_existing_one_does_not_already_exist_when_a_money_deposited_event_is_not_associated_with_an_active_transfer() {
        when(activeTransferStorage.exists(TransactionUtils.TRANSACTION_ID)).thenReturn(Mono.just(false));
        when(monthlyReportStorage.fetch(transactionMonth)).thenReturn(Mono.empty());
        when(monthlyReportStorage.save(any())).thenReturn(Mono.empty());

        projection.on(TransactionUtils.moneyDepositedIntoAccountEvent().build(), REGULAR_ACCOUNT_AGGREGATE_TYPE);

        MonthlyReport expectedReport = MonthlyReport.builder().month(transactionMonth).incoming(TransactionUtils.ANOTHER_TRANSACTION_AMOUNT).build();
        verify(monthlyReportStorage).save(expectedReport);
    }

    @Test
    @DisplayName("it should ignore the incoming money of a money deposited event that is associated with an active transfer")
    void it_should_ignore_the_incoming_money_of_a_money_deposited_event_that_is_associated_with_an_active_transfer() {
        when(activeTransferStorage.exists(TransactionUtils.TRANSACTION_ID)).thenReturn(Mono.just(true));
        when(activeTransferStorage.delete(TransactionUtils.TRANSACTION_ID)).thenReturn(Mono.empty());

        projection.on(TransactionUtils.moneyDepositedIntoAccountEvent().build(), REGULAR_ACCOUNT_AGGREGATE_TYPE);
        verifyNoInteractions(monthlyReportStorage);
    }

    @Test
    @DisplayName("it should delete the active transfer from the storage when receiving a money deposited event that is associated with an active transfer")
    void it_should_delete_the_active_transfer_from_the_storage_when_receiving_a_money_deposited_event_that_is_associated_with_an_active_transfer() {
        when(activeTransferStorage.exists(TransactionUtils.TRANSACTION_ID)).thenReturn(Mono.just(true));
        when(activeTransferStorage.delete(TransactionUtils.TRANSACTION_ID)).thenReturn(Mono.empty());

        projection.on(TransactionUtils.moneyDepositedIntoAccountEvent().build(), REGULAR_ACCOUNT_AGGREGATE_TYPE);

        verify(activeTransferStorage).delete(TransactionUtils.TRANSACTION_ID);
    }

    @Test
    @DisplayName("it should delete the active transfer from the storage when receiving a money deposited event that is associated with an active transfer even if the account type is not regular")
    void it_should_delete_the_active_transfer_from_the_storage_when_receiving_a_money_deposited_event_that_is_associated_with_an_active_transfer_even_if_the_account_type_is_not_regular() {
        when(activeTransferStorage.exists(TransactionUtils.TRANSACTION_ID)).thenReturn(Mono.just(true));
        when(activeTransferStorage.delete(TransactionUtils.TRANSACTION_ID)).thenReturn(Mono.empty());

        projection.on(TransactionUtils.moneyDepositedIntoAccountEvent().build(), UUID.randomUUID().toString());

        verify(activeTransferStorage).delete(TransactionUtils.TRANSACTION_ID);
    }

    @Test
    @DisplayName("it should not track the expenses when receiving a money withdraw event when the account type is not regular")
    void it_should_not_track_the_expenses_when_receiving_a_money_withdraw_event_when_the_account_type_is_not_regular() {
        when(activeTransferStorage.exists(TransactionUtils.TRANSACTION_ID)).thenReturn(Mono.just(false));
        when(monthlyReportStorage.fetch(transactionMonth)).thenReturn(Mono.just(MonthlyReportUtils.monthlyReport().build()));
        when(monthlyReportStorage.save(any())).thenReturn(Mono.empty());

        projection.on(TransactionUtils.moneyWithdrewFromAccountEvent().build(), UUID.randomUUID().toString());
        verifyNoInteractions(monthlyReportStorage);
    }

    @Test
    @DisplayName("it should track the expenses when receiving a money withdrew event that is not associated with an active transfer")
    void it_should_track_the_expenses_when_receiving_a_money_withdrew_event_that_is_not_associated_with_an_active_transfer() {
        when(activeTransferStorage.exists(TransactionUtils.TRANSACTION_ID)).thenReturn(Mono.just(false));
        when(monthlyReportStorage.fetch(transactionMonth)).thenReturn(Mono.just(MonthlyReportUtils.monthlyReport().build()));
        when(monthlyReportStorage.save(any())).thenReturn(Mono.empty());

        projection.on(TransactionUtils.moneyWithdrewFromAccountEvent().build(), REGULAR_ACCOUNT_AGGREGATE_TYPE);

        MonthlyReport expectedReport = MonthlyReportUtils.monthlyReport()
                .expenses(MonthlyReportUtils.EXPENSES.add(TransactionUtils.ANOTHER_TRANSACTION_AMOUNT))
                .build();
        verify(monthlyReportStorage).save(expectedReport);
    }

    @Test
    @DisplayName("it should save a new report in the storage when an existing one does not already exist upon receiving a money withdrew event that is not associated with an active transfer")
    void it_should_save_a_new_report_in_the_storage_when_an_existing_one_does_not_already_exist_upon_receiving_a_money_withdrew_event_that_is_not_associated_with_an_active_transfer() {
        when(activeTransferStorage.exists(TransactionUtils.TRANSACTION_ID)).thenReturn(Mono.just(false));
        when(monthlyReportStorage.fetch(transactionMonth)).thenReturn(Mono.empty());
        when(monthlyReportStorage.save(any())).thenReturn(Mono.empty());

        projection.on(TransactionUtils.moneyWithdrewFromAccountEvent().build(), REGULAR_ACCOUNT_AGGREGATE_TYPE);

        MonthlyReport expectedReport = MonthlyReport.builder().month(transactionMonth).expenses(TransactionUtils.ANOTHER_TRANSACTION_AMOUNT).build();
        verify(monthlyReportStorage).save(expectedReport);
    }

    @Test
    @DisplayName("it should ignore any money withdrew event that is associated with an active transfer")
    void it_should_ignore_any_money_withdrew_event_that_is_associated_with_an_active_transfer() {
        when(activeTransferStorage.exists(TransactionUtils.TRANSACTION_ID)).thenReturn(Mono.just(true));

        projection.on(TransactionUtils.moneyWithdrewFromAccountEvent().build(), REGULAR_ACCOUNT_AGGREGATE_TYPE);
        verifyNoInteractions(monthlyReportStorage);
    }
}