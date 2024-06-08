package me.ezzedine.mohammed.xpenser.core.account.budget;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CurrenciesTest {

    @Test
    @DisplayName("it returns the correct representation of the dollar currency")
    void it_returns_the_correct_representation_of_the_dollar_currency() {
        Currency dollar = Currencies.dollar();
        assertEquals(CurrencyCode.USD, dollar.code());
        assertEquals("$", dollar.symbol());
        assertEquals("United States Dollar", dollar.name());
    }

    @Test
    @DisplayName("it returns the correct representation of the euro currency")
    void it_returns_the_correct_representation_of_the_euro_currency() {
        Currency euro = Currencies.euro();
        assertEquals(CurrencyCode.EUR, euro.code());
        assertEquals("€", euro.symbol());
        assertEquals("Euro", euro.name());
    }

    @Test
    @DisplayName("it returns the correct representation of the swiss franc currency")
    void it_returns_the_correct_representation_of_the_swiss_franc_currency() {
        Currency swissFranc = Currencies.swissFranc();
        assertEquals(CurrencyCode.CHF, swissFranc.code());
        assertEquals("CHF", swissFranc.symbol());
        assertEquals("Swiss Franc", swissFranc.name());
    }

    @Test
    @DisplayName("it returns the correct representation of the lebanese lira currency")
    void it_returns_the_correct_representation_of_the_lebanese_lira_currency() {
        Currency lebaneseLira = Currencies.lebaneseLira();
        assertEquals(CurrencyCode.LBP, lebaneseLira.code());
        assertEquals("LBP", lebaneseLira.symbol());
        assertEquals("Lebanese Lira", lebaneseLira.name());
    }

    @ParameterizedTest
    @EnumSource(CurrencyCode.class)
    @DisplayName("it returns the correct currency given its code")
    void it_returns_the_correct_currency_given_its_code(CurrencyCode code) {
        assertEquals(code, Currencies.fromCode(code.toString()).code());
    }
}