package me.ezzedine.mohammed.xpenser.api.expense;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record MonthlyReportApiResponse(
    String first,
    String last,
    Report report
) {

    public record Report(
        String month,
        BigDecimal incoming,
        BigDecimal expenses,
        BigDecimal target
    ) { }
}
