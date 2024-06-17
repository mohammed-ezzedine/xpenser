package me.ezzedine.mohammed.xpenser.infra.persistence.expense;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.YearMonth;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "monthly_report")
public class MonthlyReportDocument {

    @Id
    private YearMonth month;
    private BigDecimal incoming;
    private BigDecimal expenses;
    private BigDecimal target;
}
