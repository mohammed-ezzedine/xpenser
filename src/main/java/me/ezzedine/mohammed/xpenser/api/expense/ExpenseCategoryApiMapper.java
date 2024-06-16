package me.ezzedine.mohammed.xpenser.api.expense;

import me.ezzedine.mohammed.xpenser.core.expense.CreateExpenseCategoryRequest;
import me.ezzedine.mohammed.xpenser.core.expense.ExpenseCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExpenseCategoryApiMapper {

    ExpenseCategoryApiModel map(ExpenseCategory category);
    CreateExpenseCategoryRequest map(CreateExpenseCategoryApiRequest request);
}
