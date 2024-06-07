package me.ezzedine.mohammed.xpenser.core.account.budget;

import java.util.Random;
import java.util.UUID;

public class CurrencyUtils {

    public static CurrencyCode currencyCode() {
        return CurrencyCode.values()[new Random().nextInt(CurrencyCode.values().length)];
    }

    public static Currency currency() {
        return new Currency(currencyCode(), UUID.randomUUID().toString(), UUID.randomUUID().toString());
    }
}
