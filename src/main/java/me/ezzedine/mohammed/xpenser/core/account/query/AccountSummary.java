package me.ezzedine.mohammed.xpenser.core.account.query;

import lombok.NonNull;

public record AccountSummary(
    @NonNull String id,
    @NonNull String name,
    @NonNull BudgetSummary budget
) { }
