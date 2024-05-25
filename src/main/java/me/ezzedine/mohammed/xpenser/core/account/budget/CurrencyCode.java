package me.ezzedine.mohammed.xpenser.core.account.budget;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum CurrencyCode {
    DOLLAR("USD"),
    EURO("EUR"),
    SWISS_FRANC("CHF"),
    LEBANESE_LIRA("LBP");

    private final String value;

    public static CurrencyCode fromString(String code) {
        return Arrays.stream(CurrencyCode.values()).filter(c -> c.value.equals(code))
                .findFirst()
                .orElseThrow();
    }
}
