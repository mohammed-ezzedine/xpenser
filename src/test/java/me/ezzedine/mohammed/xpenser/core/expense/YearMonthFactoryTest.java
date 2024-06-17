package me.ezzedine.mohammed.xpenser.core.expense;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class YearMonthFactoryTest {

    @Test
    @DisplayName("returns a non null month")
    void returns_a_non_null_month() {
        assertNotNull(new YearMonthFactory().now());
    }

}