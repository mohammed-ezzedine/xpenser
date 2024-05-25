package me.ezzedine.mohammed.xpenser.core.account.opening;

import me.ezzedine.mohammed.xpenser.core.account.budget.Budget;

import java.util.Date;

public record AccountOpenedEvent(
    String id,
    String name,
    Budget budget,
    Date timestamp
) { }
