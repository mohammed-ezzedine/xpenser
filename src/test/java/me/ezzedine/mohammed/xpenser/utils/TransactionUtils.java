package me.ezzedine.mohammed.xpenser.utils;

import me.ezzedine.mohammed.xpenser.core.account.transactions.MoneyDepositedInAccountEvent;
import me.ezzedine.mohammed.xpenser.core.account.transactions.MoneyWithdrewFromAccountEvent;
import me.ezzedine.mohammed.xpenser.core.account.transactions.query.TransactionSummary;
import me.ezzedine.mohammed.xpenser.infra.persistence.account.transaction.TransactionDocumentEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class TransactionUtils {
    public static final String TRANSACTION_ID = UUID.randomUUID().toString();
    public static final BigDecimal TRANSACTION_AMOUNT = BigDecimal.valueOf(new Random().nextDouble(0, 100));
    public static final BigDecimal ANOTHER_TRANSACTION_AMOUNT = BigDecimal.valueOf(new Random().nextDouble(0, 100));
    public static final String TRANSACTION_NOTE = UUID.randomUUID().toString();
    public static final String ANOTHER_TRANSACTION_NOTE = UUID.randomUUID().toString();
    private static final Date TRANSACTION_DATE = Date.from(Instant.parse("2024-06-08T14:02:52.651Z"));
    private static final Date ANOTHER_TRANSACTION_DATE = Date.from(Instant.parse("2025-07-09T13:01:53.237Z"));


    public static MoneyDepositedInAccountEvent moneyDepositedIntoAccountEvent() {
        return new MoneyDepositedInAccountEvent(TRANSACTION_ID, AccountUtils.ACCOUNT_ID, TRANSACTION_AMOUNT, TRANSACTION_NOTE, TRANSACTION_DATE);
    }

    public static MoneyWithdrewFromAccountEvent moneyWithdrewFromAccountEvent() {
        return new MoneyWithdrewFromAccountEvent(TRANSACTION_ID, AccountUtils.ACCOUNT_ID, TRANSACTION_AMOUNT, TRANSACTION_NOTE, TRANSACTION_DATE);
    }

    public static TransactionSummary.TransactionSummaryBuilder transactionSummary() {
        return TransactionSummary.builder().amount(TRANSACTION_AMOUNT).balance(BudgetUtils.BUDGET_AMOUNT).note(TRANSACTION_NOTE).timestamp(TRANSACTION_DATE);
    }

    public static TransactionSummary.TransactionSummaryBuilder openingTransactionSummary() {
        return TransactionSummary.builder().amount(BudgetUtils.BUDGET_AMOUNT).balance(BudgetUtils.BUDGET_AMOUNT).note("Account opened.").timestamp(AccountUtils.ACCOUNT_CREATION_DATE);
    }

    public static TransactionSummary.TransactionSummaryBuilder anotherTransactionSummary() {
        return TransactionSummary.builder().amount(ANOTHER_TRANSACTION_AMOUNT).balance(BudgetUtils.ANOTHER_BUDGET_AMOUNT).note(ANOTHER_TRANSACTION_NOTE).timestamp(ANOTHER_TRANSACTION_DATE);
    }

    public static TransactionDocumentEntity.TransactionDocumentEntityBuilder transactionDocument() {
        return TransactionDocumentEntity.builder().amount(TRANSACTION_AMOUNT).balance(BudgetUtils.BUDGET_AMOUNT).note(TRANSACTION_NOTE).timestamp(TRANSACTION_DATE);
    }

    public static TransactionDocumentEntity.TransactionDocumentEntityBuilder anotherTransactionDocument() {
        return TransactionDocumentEntity.builder().amount(ANOTHER_TRANSACTION_AMOUNT).balance(BudgetUtils.ANOTHER_BUDGET_AMOUNT).note(ANOTHER_TRANSACTION_NOTE).timestamp(ANOTHER_TRANSACTION_DATE);
    }
}
