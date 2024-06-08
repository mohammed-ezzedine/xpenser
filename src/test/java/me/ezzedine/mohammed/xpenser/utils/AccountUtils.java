package me.ezzedine.mohammed.xpenser.utils;

import me.ezzedine.mohammed.xpenser.core.account.query.AccountSummary;
import me.ezzedine.mohammed.xpenser.infra.persistence.account.AccountSummaryDocument;

import java.util.UUID;

public class AccountUtils {

    public static final String ACCOUNT_ID = UUID.randomUUID().toString();
    public static final String ANOTHER_ACCOUNT_ID = UUID.randomUUID().toString();
    public static final String ACCOUNT_NAME = UUID.randomUUID().toString();
    public static final String ANOTHER_ACCOUNT_NAME = UUID.randomUUID().toString();

    public static AccountSummary.AccountSummaryBuilder accountSummary() {
        return AccountSummary.builder().id(ACCOUNT_ID).name(ACCOUNT_NAME).budget(BudgetUtils.budgetSummary());
    }

    public static AccountSummaryDocument.AccountSummaryDocumentBuilder accountSummaryDocument() {
        return AccountSummaryDocument.builder().id(ACCOUNT_ID).name(ACCOUNT_NAME).currencyCode(CurrencyUtils.CURRENCY_CODE.toString()).amount(BudgetUtils.BUDGET_AMOUNT);
    }

    public static AccountSummaryDocument.AccountSummaryDocumentBuilder anotherAccountSummaryDocument() {
        return AccountSummaryDocument.builder().id(ANOTHER_ACCOUNT_ID).name(ANOTHER_ACCOUNT_NAME).currencyCode(CurrencyUtils.ANOTHER_CURRENCY_CODE.toString()).amount(BudgetUtils.ANOTHER_BUDGET_AMOUNT);
    }
}
