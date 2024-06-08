package me.ezzedine.mohammed.xpenser.utils;

import me.ezzedine.mohammed.xpenser.core.account.opening.AccountOpenedEvent;
import me.ezzedine.mohammed.xpenser.core.account.projection.summary.AccountSummary;
import me.ezzedine.mohammed.xpenser.infra.persistence.account.AccountSummaryDocument;

import java.util.Date;
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

    public static AccountSummaryDocument.AccountSummaryDocumentBuilder accountSummaryDocument() {
        return AccountSummaryDocument.builder().id(ACCOUNT_ID).name(ACCOUNT_NAME).currencyCode(CurrencyUtils.CURRENCY_CODE.toString()).amount(BudgetUtils.BUDGET_AMOUNT);
    }

    public static AccountSummaryDocument.AccountSummaryDocumentBuilder anotherAccountSummaryDocument() {
        return AccountSummaryDocument.builder().id(ANOTHER_ACCOUNT_ID).name(ANOTHER_ACCOUNT_NAME).currencyCode(CurrencyUtils.ANOTHER_CURRENCY_CODE.toString()).amount(BudgetUtils.ANOTHER_BUDGET_AMOUNT);
    }

    public static AccountOpenedEvent accountOpenedEvent() {
        return new AccountOpenedEvent(ACCOUNT_ID, ACCOUNT_NAME, BudgetUtils.budget(), ACCOUNT_CREATION_DATE);
    }
}