package me.ezzedine.mohammed.xpenser.core.expense;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.YearMonth;

@Data
@Builder(toBuilder = true)
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

    public MonthlyReport addIncome(BigDecimal amount) {
        return this.toBuilder().incoming(incoming.add(amount)).build();
    }

    public MonthlyReport addExpense(BigDecimal amount) {
        return this.toBuilder().expenses(expenses.add(amount)).build();
    }

}
