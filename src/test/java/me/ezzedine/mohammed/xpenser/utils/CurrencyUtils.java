package me.ezzedine.mohammed.xpenser.utils;

import me.ezzedine.mohammed.xpenser.core.account.budget.Currency;
import me.ezzedine.mohammed.xpenser.core.account.budget.CurrencyCode;

import java.util.Random;
import java.util.UUID;

public class CurrencyUtils {

    public static final CurrencyCode CURRENCY_CODE = CurrencyCode.values()[new Random().nextInt(CurrencyCode.values().length)];
    public static final CurrencyCode ANOTHER_CURRENCY_CODE = CurrencyCode.values()[new Random().nextInt(CurrencyCode.values().length)];

    public static Currency currency() {
        return new Currency(CURRENCY_CODE, UUID.randomUUID().toString(), UUID.randomUUID().toString());
    }
}
