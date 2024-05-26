package me.ezzedine.mohammed.xpenser.core.account.query;

import javax.annotation.Nonnull;

public record AccountSummary(
    @Nonnull String id,
    @Nonnull String name,
    @Nonnull BudgetSummary budget
) { }
