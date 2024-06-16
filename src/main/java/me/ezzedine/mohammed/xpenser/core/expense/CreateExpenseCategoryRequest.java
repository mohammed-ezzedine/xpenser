package me.ezzedine.mohammed.xpenser.core.expense;

import lombok.Builder;

@Builder
public record CreateExpenseCategoryRequest(
    String name
) {
}
