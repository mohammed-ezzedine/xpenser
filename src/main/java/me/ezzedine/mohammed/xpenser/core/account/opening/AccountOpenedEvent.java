package me.ezzedine.mohammed.xpenser.core.account.opening;

import me.ezzedine.mohammed.xpenser.core.account.budget.Budget;

import javax.annotation.Nonnull;
import java.util.Date;

public record AccountOpenedEvent(
    @Nonnull String id,
    @Nonnull String name,
    @Nonnull Budget budget,
    @Nonnull Date timestamp
) { }
