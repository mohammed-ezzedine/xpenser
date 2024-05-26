package me.ezzedine.mohammed.xpenser.core.account.query;

import javax.annotation.Nonnull;

public record Currency (
    @Nonnull String code,
    @Nonnull String symbol,
    @Nonnull String name
) {
    public static Currency from(me.ezzedine.mohammed.xpenser.core.account.budget.Currency currency) {
        return new Currency(currency.code().getValue(), currency.symbol(), currency.name());
    }
}
