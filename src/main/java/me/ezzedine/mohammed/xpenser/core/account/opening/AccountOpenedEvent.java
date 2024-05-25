package me.ezzedine.mohammed.xpenser.core.account.opening;

import me.ezzedine.mohammed.xpenser.core.account.budget.Budget;

public record AccountOpenedEvent(
    String id,
    String name,
    Budget budget
) { }
