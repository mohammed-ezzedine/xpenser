package me.ezzedine.mohammed.xpenser.core.expense.category;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record CreateExpenseCategoryRequest(
    @NonNull String name,
    @NonNull String icon
) {
}
