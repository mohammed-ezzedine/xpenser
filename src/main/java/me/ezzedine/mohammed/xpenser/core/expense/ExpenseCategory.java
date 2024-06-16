package me.ezzedine.mohammed.xpenser.core.expense;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record ExpenseCategory(
    @NonNull String id,
    @NonNull String name
) {
}
