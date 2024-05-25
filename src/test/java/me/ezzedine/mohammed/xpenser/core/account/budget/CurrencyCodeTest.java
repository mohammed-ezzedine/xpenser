package me.ezzedine.mohammed.xpenser.core.account.budget;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CurrencyCodeTest {

    @ParameterizedTest
    @EnumSource(CurrencyCode.class)
    @DisplayName("returns the correct currency given its code")
    void returns_the_correct_currency_given_its_code(CurrencyCode code) {
        assertEquals(code, CurrencyCode.fromString(code.getValue()));
    }

    @Test
    @DisplayName("throws an exception when the code is not recognized")
    void throws_an_exception_when_the_code_is_not_recognized() {
        assertThrows(RuntimeException.class, () -> CurrencyCode.fromString(UUID.randomUUID().toString()));
    }

}