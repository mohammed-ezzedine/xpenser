package me.ezzedine.mohammed.xpenser.core.expense;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.YearMonth;

@Data
@Builder
public class MonthlyReport {

    @NonNull
    private YearMonth month;

    @NonNull
    @Builder.Default
    private BigDecimal incoming = BigDecimal.ZERO;

    @NonNull
    @Builder.Default
    private BigDecimal expenses = BigDecimal.ZERO;

    private BigDecimal target;

    public void addIncome(BigDecimal amount) {
        incoming = incoming.add(amount);
    }

    public void addExpense(BigDecimal amount) {
        expenses = expenses.add(amount);
    }

}
