package me.ezzedine.mohammed.xpenser.core.account.budget;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CurrenciesTest {

    @Test
    @DisplayName("it returns the correct representation of the dollar currency")
    void it_returns_the_correct_representation_of_the_dollar_currency() {
        Currency dollar = Currencies.dollar();
        assertEquals("USD", dollar.id());
        assertEquals("$", dollar.symbol());
    }

    @Test
    @DisplayName("it returns the correct representation of the euro currency")
    void it_returns_the_correct_representation_of_the_euro_currency() {
        Currency euro = Currencies.euro();
        assertEquals("EUR", euro.id());
        assertEquals("€", euro.symbol());
    }

    @Test
    @DisplayName("it returns the correct representation of the swiss franc currency")
    void it_returns_the_correct_representation_of_the_swiss_franc_currency() {
        Currency swissFranc = Currencies.swissFranc();
        assertEquals("CHF", swissFranc.id());
        assertEquals("CHF", swissFranc.symbol());
    }

    @Test
    @DisplayName("it returns the correct representation of the lebanese lira currency")
    void it_returns_the_correct_representation_of_the_lebanese_lira_currency() {
        Currency lebaneseLira = Currencies.lebaneseLira();
        assertEquals("LBP", lebaneseLira.id());
        assertEquals("LBP", lebaneseLira.symbol());
    }
}