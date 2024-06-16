package me.ezzedine.mohammed.xpenser.core.expense;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExpenseCategoryMapper {

    ExpenseCategory map(CreateExpenseCategoryRequest request, String id);
}
