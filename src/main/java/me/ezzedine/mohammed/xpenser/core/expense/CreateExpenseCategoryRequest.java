package me.ezzedine.mohammed.xpenser.core.expense;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record CreateExpenseCategoryRequest(
    @NonNull String name,
    @NonNull String icon
) {
}
