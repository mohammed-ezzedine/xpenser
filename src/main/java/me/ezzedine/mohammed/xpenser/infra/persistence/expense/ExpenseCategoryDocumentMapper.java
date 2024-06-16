package me.ezzedine.mohammed.xpenser.infra.persistence.expense;

import me.ezzedine.mohammed.xpenser.core.expense.ExpenseCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExpenseCategoryDocumentMapper {
    ExpenseCategoryDocument map(ExpenseCategory category);
    ExpenseCategory map(ExpenseCategoryDocument category);
}
