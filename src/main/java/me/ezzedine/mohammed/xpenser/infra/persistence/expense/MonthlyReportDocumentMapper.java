package me.ezzedine.mohammed.xpenser.infra.persistence.expense;

import me.ezzedine.mohammed.xpenser.core.expense.MonthlyReport;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MonthlyReportDocumentMapper {

    MonthlyReportDocument map(MonthlyReport report);
    MonthlyReport map(MonthlyReportDocument report);
}
