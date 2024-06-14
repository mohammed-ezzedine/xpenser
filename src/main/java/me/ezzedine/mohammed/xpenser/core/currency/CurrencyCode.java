package me.ezzedine.mohammed.xpenser.core.currency;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CurrencyCode {
    USD, EUR, CHF, LBP;

    @JsonCreator
    public static CurrencyCode fromJson(String code) {
        return CurrencyCode.valueOf(code.toUpperCase());
    }

    @JsonValue
    public String toJson() {
        return this.name();
    }
}
