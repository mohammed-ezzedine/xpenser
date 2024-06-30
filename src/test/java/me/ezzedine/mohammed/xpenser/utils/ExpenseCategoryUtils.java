package me.ezzedine.mohammed.xpenser.utils;

import me.ezzedine.mohammed.xpenser.api.expense.category.CreateExpenseCategoryApiRequest;
import me.ezzedine.mohammed.xpenser.core.expense.category.CreateExpenseCategoryRequest;
import me.ezzedine.mohammed.xpenser.core.expense.category.ExpenseCategory;
import me.ezzedine.mohammed.xpenser.core.expense.category.report.ExpenseCategoryMonthlyReport;
import me.ezzedine.mohammed.xpenser.infra.persistence.expense.category.ExpenseCategoryDocument;
import me.ezzedine.mohammed.xpenser.infra.persistence.expense.category.report.ExpenseCategoryMonthlyReportDocument;

import java.math.BigDecimal;
import java.time.Month;
import java.time.YearMonth;
import java.util.Random;
import java.util.UUID;

public class ExpenseCategoryUtils {

    public static final String EXPENSE_CATEGORY_ID = UUID.randomUUID().toString();
    public static final String ANOTHER_EXPENSE_CATEGORY_ID = UUID.randomUUID().toString();
    public static final String EXPENSE_CATEGORY_NAME = UUID.randomUUID().toString();
    public static final String ANOTHER_EXPENSE_CATEGORY_NAME = UUID.randomUUID().toString();
    public static final String EXPENSE_CATEGORY_ICON = UUID.randomUUID().toString();
    public static final String ANOTHER_EXPENSE_CATEGORY_ICON = UUID.randomUUID().toString();
    public static final BigDecimal REPORT_AMOUNT = BigDecimal.valueOf(new Random().nextDouble(10, 100));
    public static final BigDecimal ANOTHER_REPORT_AMOUNT = BigDecimal.valueOf(new Random().nextDouble(10, 100));
    public static final YearMonth REPORT_MONTH = YearMonth.of(2024, Month.DECEMBER);
    public static final YearMonth ANOTHER_REPORT_MONTH = YearMonth.of(2024, Month.NOVEMBER);

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

    public static ExpenseCategoryMonthlyReport.ExpenseCategoryMonthlyReportBuilder monthlyReport() {
        return ExpenseCategoryMonthlyReport.builder().category(EXPENSE_CATEGORY_ID).amount(REPORT_AMOUNT).month(REPORT_MONTH);
    }

    public static ExpenseCategoryMonthlyReport.ExpenseCategoryMonthlyReportBuilder anotherMonthlyReport() {
        return ExpenseCategoryMonthlyReport.builder().category(ANOTHER_EXPENSE_CATEGORY_ID).amount(ANOTHER_REPORT_AMOUNT).month(ANOTHER_REPORT_MONTH);
    }

    public static ExpenseCategoryMonthlyReportDocument.ExpenseCategoryMonthlyReportDocumentBuilder monthlyReportDocument() {
        return ExpenseCategoryMonthlyReportDocument.builder().id(monthlyReportDocumentId().build()).amount(REPORT_AMOUNT);
    }

    public static ExpenseCategoryMonthlyReportDocument.CompositeKey.CompositeKeyBuilder monthlyReportDocumentId() {
        return ExpenseCategoryMonthlyReportDocument.CompositeKey.builder().category(EXPENSE_CATEGORY_ID).month(REPORT_MONTH);
    }

    public static ExpenseCategoryMonthlyReportDocument.ExpenseCategoryMonthlyReportDocumentBuilder anotherMonthlyReportDocument() {
        return ExpenseCategoryMonthlyReportDocument.builder().id(anotherMonthlyReportDocumentId().build()).amount(ANOTHER_REPORT_AMOUNT);
    }

    public static ExpenseCategoryMonthlyReportDocument.CompositeKey.CompositeKeyBuilder anotherMonthlyReportDocumentId() {
        return ExpenseCategoryMonthlyReportDocument.CompositeKey.builder().category(ANOTHER_EXPENSE_CATEGORY_ID).month(ANOTHER_REPORT_MONTH);
    }
}
