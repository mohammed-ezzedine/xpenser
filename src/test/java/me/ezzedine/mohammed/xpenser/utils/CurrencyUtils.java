package me.ezzedine.mohammed.xpenser.utils;

import me.ezzedine.mohammed.xpenser.core.currency.CurrencyCode;

import java.util.Random;

public class CurrencyUtils {

    private static final CurrencyCode CURRENCY_CODE = CurrencyCode.values()[new Random().nextInt(CurrencyCode.values().length)];
    private static final CurrencyCode ANOTHER_CURRENCY_CODE = CurrencyCode.values()[new Random().nextInt(CurrencyCode.values().length)];

    public static CurrencyCode currencyCode() {
        return CURRENCY_CODE;
    }

    public static CurrencyCode anotherCurrencyCode() {
        return ANOTHER_CURRENCY_CODE;
    }
}
