package me.ezzedine.mohammed.xpenser.core.account.budget;

import java.util.Map;

public class Currencies {

    private static final Map<CurrencyCode, Currency> SUPPORTED_CURRENCIES = Map.of(
            CurrencyCode.DOLLAR, dollar(),
            CurrencyCode.EURO, euro(),
            CurrencyCode.SWISS_FRANC, swissFranc(),
            CurrencyCode.LEBANESE_LIRA, lebaneseLira()
    );

    public static Currency dollar() {
        return new Currency(CurrencyCode.DOLLAR, "$", "United States Dollar");
    }

    public static Currency euro() {
        return new Currency(CurrencyCode.EURO, "€", "Euro");
    }

    public static Currency swissFranc() {
        return new Currency(CurrencyCode.SWISS_FRANC, "CHF", "Swiss Franc");
    }

    public static Currency lebaneseLira() {
        return new Currency(CurrencyCode.LEBANESE_LIRA, "LBP", "Lebanese Lira");
    }

    public static Currency fromCode(String code) {
        return SUPPORTED_CURRENCIES.get(CurrencyCode.fromString(code));
    }
}
