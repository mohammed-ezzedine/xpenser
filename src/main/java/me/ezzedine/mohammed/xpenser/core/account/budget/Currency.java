package me.ezzedine.mohammed.xpenser.core.account.budget;

import lombok.NonNull;

public record Currency (
    @NonNull CurrencyCode code,
    @NonNull String symbol,
    @NonNull String name
) { }
