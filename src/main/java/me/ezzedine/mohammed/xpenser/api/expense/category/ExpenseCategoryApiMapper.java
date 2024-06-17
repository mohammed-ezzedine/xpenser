package me.ezzedine.mohammed.xpenser.api.expense.category;

import me.ezzedine.mohammed.xpenser.core.expense.category.CreateExpenseCategoryRequest;
import me.ezzedine.mohammed.xpenser.core.expense.category.ExpenseCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExpenseCategoryApiMapper {

    ExpenseCategoryApiModel map(ExpenseCategory category);
    CreateExpenseCategoryRequest map(CreateExpenseCategoryApiRequest request);
}
