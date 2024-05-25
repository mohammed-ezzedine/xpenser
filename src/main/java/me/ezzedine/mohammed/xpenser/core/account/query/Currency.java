package me.ezzedine.mohammed.xpenser.core.account.query;

public record Currency (
    String code,
    String symbol,
    String name
) {
    public static Currency from(me.ezzedine.mohammed.xpenser.core.account.budget.Currency currency) {
        return new Currency(currency.code().getValue(), currency.symbol(), currency.name());
    }
}
