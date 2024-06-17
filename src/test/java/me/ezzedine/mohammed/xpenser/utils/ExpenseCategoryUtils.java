package me.ezzedine.mohammed.xpenser.utils;

import me.ezzedine.mohammed.xpenser.api.expense.category.CreateExpenseCategoryApiRequest;
import me.ezzedine.mohammed.xpenser.core.expense.category.CreateExpenseCategoryRequest;
import me.ezzedine.mohammed.xpenser.core.expense.category.ExpenseCategory;
import me.ezzedine.mohammed.xpenser.infra.persistence.expense.category.ExpenseCategoryDocument;

import java.util.UUID;

public class ExpenseCategoryUtils {

    public static final String EXPENSE_CATEGORY_ID = UUID.randomUUID().toString();
    public static final String ANOTHER_EXPENSE_CATEGORY_ID = UUID.randomUUID().toString();
    public static final String EXPENSE_CATEGORY_NAME = UUID.randomUUID().toString();
    public static final String ANOTHER_EXPENSE_CATEGORY_NAME = UUID.randomUUID().toString();
    public static final String EXPENSE_CATEGORY_ICON = UUID.randomUUID().toString();
    public static final String ANOTHER_EXPENSE_CATEGORY_ICON = UUID.randomUUID().toString();

    public static ExpenseCategory.ExpenseCategoryBuilder expenseCategory() {
        return ExpenseCategory.builder().id(EXPENSE_CATEGORY_ID).name(EXPENSE_CATEGORY_NAME).icon(EXPENSE_CATEGORY_ICON);
    }

    public static ExpenseCategory.ExpenseCategoryBuilder anotherExpenseCategory() {
        return ExpenseCategory.builder().id(ANOTHER_EXPENSE_CATEGORY_ID).name(ANOTHER_EXPENSE_CATEGORY_NAME).icon(ANOTHER_EXPENSE_CATEGORY_ICON);
    }

    public static ExpenseCategoryDocument.ExpenseCategoryDocumentBuilder expenseCategoryDocument() {
        return ExpenseCategoryDocument.builder().id(EXPENSE_CATEGORY_ID).name(EXPENSE_CATEGORY_NAME).icon(EXPENSE_CATEGORY_ICON);
    }

    public static ExpenseCategoryDocument.ExpenseCategoryDocumentBuilder anotherExpenseCategoryDocument() {
        return ExpenseCategoryDocument.builder().id(ANOTHER_EXPENSE_CATEGORY_ID).name(ANOTHER_EXPENSE_CATEGORY_NAME).icon(ANOTHER_EXPENSE_CATEGORY_ICON);
    }

    public static CreateExpenseCategoryRequest.CreateExpenseCategoryRequestBuilder createExpenseCategoryRequest() {
        return CreateExpenseCategoryRequest.builder().name(EXPENSE_CATEGORY_NAME).icon(EXPENSE_CATEGORY_ICON);
    }

    public static CreateExpenseCategoryApiRequest.CreateExpenseCategoryApiRequestBuilder createExpenseCategoryApiRequest() {
        return CreateExpenseCategoryApiRequest.builder().name(EXPENSE_CATEGORY_NAME).icon(EXPENSE_CATEGORY_ICON);
    }
}
