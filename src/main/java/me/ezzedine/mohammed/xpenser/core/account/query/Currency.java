package me.ezzedine.mohammed.xpenser.core.account.query;

import lombok.NonNull;

public record Currency (
    @NonNull String code,
    @NonNull String symbol,
    @NonNull String name
) {
    public static Currency from(me.ezzedine.mohammed.xpenser.core.account.budget.Currency currency) {
        return new Currency(currency.code().getValue(), currency.symbol(), currency.name());
    }
}
