package me.ezzedine.mohammed.xpenser.core.account.projection.summary;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record AccountSummary(
    @NonNull String id,
    @NonNull String name,
    @NonNull BudgetSummary budget
) { }
