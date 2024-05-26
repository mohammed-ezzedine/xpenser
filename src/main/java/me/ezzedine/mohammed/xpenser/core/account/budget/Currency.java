package me.ezzedine.mohammed.xpenser.core.account.budget;

import javax.annotation.Nonnull;

public record Currency (
    @Nonnull CurrencyCode code,
    @Nonnull String symbol,
    @Nonnull String name
) { }
