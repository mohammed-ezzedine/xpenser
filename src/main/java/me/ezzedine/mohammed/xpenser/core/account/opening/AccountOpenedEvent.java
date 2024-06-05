package me.ezzedine.mohammed.xpenser.core.account.opening;

import lombok.NonNull;
import me.ezzedine.mohammed.xpenser.core.account.budget.Budget;

import java.util.Date;

public record AccountOpenedEvent(
        @NonNull String id,
        @NonNull String name,
        @NonNull Budget budget,
        @NonNull Date timestamp
) { }
