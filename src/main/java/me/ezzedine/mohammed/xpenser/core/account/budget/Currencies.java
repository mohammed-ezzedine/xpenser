package me.ezzedine.mohammed.xpenser.core.account.budget;

import java.util.Map;

public class Currencies {

    private static final Map<CurrencyCode, Currency> SUPPORTED_CURRENCIES = Map.of(
            CurrencyCode.USD, dollar(),
            CurrencyCode.EUR, euro(),
            CurrencyCode.CHF, swissFranc(),
            CurrencyCode.LBP, lebaneseLira()
    );

    public static Currency dollar() {
        return new Currency(CurrencyCode.USD, "$", "United States Dollar");
    }

    public static Currency euro() {
        return new Currency(CurrencyCode.EUR, "€", "Euro");
    }

    public static Currency swissFranc() {
        return new Currency(CurrencyCode.CHF, "CHF", "Swiss Franc");
    }

    public static Currency lebaneseLira() {
        return new Currency(CurrencyCode.LBP, "LBP", "Lebanese Lira");
    }

    public static Currency fromCode(String code) {
        return SUPPORTED_CURRENCIES.get(CurrencyCode.valueOf(code));
    }
}
