package me.ezzedine.mohammed.xpenser.infra.persistence.expense.category;

import me.ezzedine.mohammed.xpenser.core.expense.category.ExpenseCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExpenseCategoryDocumentMapper {
    ExpenseCategoryDocument map(ExpenseCategory category);
    ExpenseCategory map(ExpenseCategoryDocument category);
}
