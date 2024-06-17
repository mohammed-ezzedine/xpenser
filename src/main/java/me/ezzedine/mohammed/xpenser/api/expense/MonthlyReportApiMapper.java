package me.ezzedine.mohammed.xpenser.api.expense;

import me.ezzedine.mohammed.xpenser.core.expense.MonthlyReport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MonthlyReportApiMapper {

    @Mapping(target = "month", expression = "java(report.getMonth().toString())")
    MonthlyReportApiResponse.Report map(MonthlyReport report);
}
