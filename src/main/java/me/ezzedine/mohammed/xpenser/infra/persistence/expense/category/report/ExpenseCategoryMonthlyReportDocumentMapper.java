package me.ezzedine.mohammed.xpenser.infra.persistence.expense.category.report;

import me.ezzedine.mohammed.xpenser.core.expense.category.report.ExpenseCategoryMonthlyReport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExpenseCategoryMonthlyReportDocumentMapper {

    @Mapping(target = "id.category", source = "category")
    @Mapping(target = "id.month", source = "month")
    ExpenseCategoryMonthlyReportDocument map(ExpenseCategoryMonthlyReport report);

    @Mapping(target = "category", source = "id.category")
    @Mapping(target = "month", source = "id.month")
    ExpenseCategoryMonthlyReport map(ExpenseCategoryMonthlyReportDocument report);
}
