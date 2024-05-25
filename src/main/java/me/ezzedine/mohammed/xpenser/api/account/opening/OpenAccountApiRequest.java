package me.ezzedine.mohammed.xpenser.api.account.opening;

public record OpenAccountApiRequest(
        String name,
        String currency,
        double initialAmount
) { }
