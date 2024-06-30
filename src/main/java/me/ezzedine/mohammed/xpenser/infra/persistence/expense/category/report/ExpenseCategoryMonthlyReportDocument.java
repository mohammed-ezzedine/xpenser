package me.ezzedine.mohammed.xpenser.infra.persistence.expense.category.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.YearMonth;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "expense_category_monthly_report")
public class ExpenseCategoryMonthlyReportDocument {

    @Id
    private CompositeKey id;

    private BigDecimal amount;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CompositeKey implements Serializable {
        private String category;
        private YearMonth month;
    }
}
