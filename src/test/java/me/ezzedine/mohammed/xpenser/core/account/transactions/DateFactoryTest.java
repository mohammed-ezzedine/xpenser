package me.ezzedine.mohammed.xpenser.core.account.transactions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DateFactoryTest {

    @Test
    @DisplayName("generates a non null date")
    void generates_a_non_null_date() {
        assertNotNull(new DateFactory().now());
    }
}