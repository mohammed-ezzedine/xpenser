package me.ezzedine.mohammed.xpenser.utils;

import me.ezzedine.mohammed.xpenser.core.expense.CreateExpenseCategoryRequest;
import me.ezzedine.mohammed.xpenser.core.expense.ExpenseCategory;
import me.ezzedine.mohammed.xpenser.infra.persistence.expense.ExpenseCategoryDocument;

import java.util.UUID;

public class ExpenseCategoryUtils {

    public static final String EXPENSE_CATEGORY_ID = UUID.randomUUID().toString();
    public static final String ANOTHER_EXPENSE_CATEGORY_ID = UUID.randomUUID().toString();
    public static final String EXPENSE_CATEGORY_NAME = UUID.randomUUID().toString();
    public static final String ANOTHER_EXPENSE_CATEGORY_NAME = UUID.randomUUID().toString();

    public static ExpenseCategory.ExpenseCategoryBuilder expenseCategory() {
        return ExpenseCategory.builder().id(EXPENSE_CATEGORY_ID).name(EXPENSE_CATEGORY_NAME);
    }

    public static ExpenseCategory.ExpenseCategoryBuilder anotherExpenseCategory() {
        return ExpenseCategory.builder().id(ANOTHER_EXPENSE_CATEGORY_ID).name(ANOTHER_EXPENSE_CATEGORY_NAME);
    }

    public static ExpenseCategoryDocument.ExpenseCategoryDocumentBuilder expenseCategoryDocument() {
        return ExpenseCategoryDocument.builder().id(EXPENSE_CATEGORY_ID).name(EXPENSE_CATEGORY_NAME);
    }

    public static ExpenseCategoryDocument.ExpenseCategoryDocumentBuilder anotherExpenseCategoryDocument() {
        return ExpenseCategoryDocument.builder().id(ANOTHER_EXPENSE_CATEGORY_ID).name(ANOTHER_EXPENSE_CATEGORY_NAME);
    }

    public static CreateExpenseCategoryRequest.CreateExpenseCategoryRequestBuilder createExpenseCategoryRequest() {
        return CreateExpenseCategoryRequest.builder().name(EXPENSE_CATEGORY_NAME);
    }
}
