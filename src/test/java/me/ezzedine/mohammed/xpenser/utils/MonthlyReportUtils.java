package me.ezzedine.mohammed.xpenser.utils;

import me.ezzedine.mohammed.xpenser.core.expense.MonthlyReport;
import me.ezzedine.mohammed.xpenser.infra.persistence.expense.MonthlyReportDocument;

import java.math.BigDecimal;
import java.time.Month;
import java.time.YearMonth;
import java.util.Random;

public class MonthlyReportUtils {

    public static final YearMonth MONTH = YearMonth.of(2024, Month.JUNE);
    public static final YearMonth ANOTHER_MONTH = YearMonth.of(2023, Month.MARCH);
    public static final BigDecimal TARGET = BigDecimal.valueOf(new Random().nextDouble(10, 100));
    public static final BigDecimal ANOTHER_TARGET = BigDecimal.valueOf(new Random().nextDouble(10, 100));
    public static final BigDecimal INCOMING = BigDecimal.valueOf(new Random().nextDouble(10, 100));
    public static final BigDecimal ANOTHER_INCOMING = BigDecimal.valueOf(new Random().nextDouble(10, 100));
    public static final BigDecimal EXPENSES = BigDecimal.valueOf(new Random().nextDouble(10, 100));
    public static final BigDecimal ANOTHER_EXPENSES = BigDecimal.valueOf(new Random().nextDouble(10, 100));

    public static MonthlyReport.MonthlyReportBuilder monthlyReport() {
        return MonthlyReport.builder().month(MONTH).target(TARGET).incoming(INCOMING).expenses(EXPENSES);
    }

    public static MonthlyReportDocument.MonthlyReportDocumentBuilder monthlyReportDocument() {
        return MonthlyReportDocument.builder().month(MONTH).target(TARGET).incoming(INCOMING).expenses(EXPENSES);
    }

    public static MonthlyReportDocument.MonthlyReportDocumentBuilder anotherMonthlyReportDocument() {
        return MonthlyReportDocument.builder().month(ANOTHER_MONTH).target(ANOTHER_TARGET).incoming(ANOTHER_INCOMING).expenses(ANOTHER_EXPENSES);
    }
}
