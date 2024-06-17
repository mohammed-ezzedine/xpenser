package me.ezzedine.mohammed.xpenser.api.expense.category;

import lombok.Builder;

@Builder
public record ExpenseCategoryApiModel(
    String id,
    String name,
    String icon
) {
}
