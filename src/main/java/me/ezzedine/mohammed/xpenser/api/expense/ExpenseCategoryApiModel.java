package me.ezzedine.mohammed.xpenser.api.expense;

import lombok.Builder;

@Builder
public record ExpenseCategoryApiModel(
    String id,
    String name
) {
}
