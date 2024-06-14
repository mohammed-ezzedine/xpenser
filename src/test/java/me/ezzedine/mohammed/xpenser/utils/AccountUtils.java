package me.ezzedine.mohammed.xpenser.utils;

import me.ezzedine.mohammed.xpenser.core.account.opening.AccountOpenedEvent;
import me.ezzedine.mohammed.xpenser.core.account.opening.OpenAccountCommand;
import me.ezzedine.mohammed.xpenser.core.account.projection.summary.AccountSummary;
import me.ezzedine.mohammed.xpenser.core.account.transactions.query.AccountTransactionSummary;
import me.ezzedine.mohammed.xpenser.infra.persistence.account.AccountSummaryDocument;
import me.ezzedine.mohammed.xpenser.infra.persistence.account.transaction.AccountTransactionsDocument;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.mock;

public class AccountUtils {

    public static final String ACCOUNT_ID = UUID.randomUUID().toString();
    public static final String ANOTHER_ACCOUNT_ID = UUID.randomUUID().toString();
    public static final String ACCOUNT_NAME = UUID.randomUUID().toString();
    public static final String ANOTHER_ACCOUNT_NAME = UUID.randomUUID().toString();
    public static final Date ACCOUNT_CREATION_DATE = mock(Date.class);

    public static AccountSummary.AccountSummaryBuilder accountSummary() {
        return AccountSummary.builder().id(ACCOUNT_ID).name(ACCOUNT_NAME).budget(BudgetUtils.budgetSummary().build());
    }

    public static AccountSummary.AccountSummaryBuilder anotherAccountSummary() {
        return AccountSummary.builder().id(ANOTHER_ACCOUNT_ID).name(ANOTHER_ACCOUNT_NAME).budget(BudgetUtils.anotherBudgetSummary().build());
    }

    public static AccountSummaryDocument.AccountSummaryDocumentBuilder accountSummaryDocument() {
        return AccountSummaryDocument.builder().id(ACCOUNT_ID).name(ACCOUNT_NAME).currencyCode(CurrencyUtils.currencyCode()).amount(BudgetUtils.BUDGET_AMOUNT);
    }

    public static AccountSummaryDocument.AccountSummaryDocumentBuilder anotherAccountSummaryDocument() {
        return AccountSummaryDocument.builder().id(ANOTHER_ACCOUNT_ID).name(ANOTHER_ACCOUNT_NAME).currencyCode(CurrencyUtils.anotherCurrencyCode()).amount(BudgetUtils.ANOTHER_BUDGET_AMOUNT);
    }

    public static OpenAccountCommand.OpenAccountCommandBuilder openAccountCommand() {
        return OpenAccountCommand.builder().name(AccountUtils.ACCOUNT_NAME).id(AccountUtils.ACCOUNT_ID)
                .currencyCode(CurrencyUtils.currencyCode()).budgetInitialAmount(BudgetUtils.BUDGET_AMOUNT).timestamp(ACCOUNT_CREATION_DATE);
    }

    public static AccountOpenedEvent accountOpenedEvent() {
        return new AccountOpenedEvent(ACCOUNT_ID, ACCOUNT_NAME, BudgetUtils.budget(), ACCOUNT_CREATION_DATE);
    }

    public static AccountTransactionSummary.AccountTransactionSummaryBuilder accountTransactionsSummary() {
        return AccountTransactionSummary.builder().id(ACCOUNT_ID).transactions(List.of(TransactionUtils.transactionSummary().build()));
    }

    public static AccountTransactionsDocument.AccountTransactionsDocumentBuilder accountTransactionsDocument() {
        return AccountTransactionsDocument.builder().id(ACCOUNT_ID).transactions(List.of(TransactionUtils.transactionDocument().build()));
    }

    public static AccountTransactionsDocument.AccountTransactionsDocumentBuilder anotherAccountTransactionsDocument() {
        return AccountTransactionsDocument.builder().id(ANOTHER_ACCOUNT_ID).transactions(List.of(TransactionUtils.anotherTransactionDocument().build()));
    }
}
