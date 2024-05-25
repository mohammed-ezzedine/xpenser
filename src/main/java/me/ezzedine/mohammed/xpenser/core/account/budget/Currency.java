package me.ezzedine.mohammed.xpenser.core.account.budget;

public record Currency (
    CurrencyCode code,
    String symbol,
    String name
) { }
