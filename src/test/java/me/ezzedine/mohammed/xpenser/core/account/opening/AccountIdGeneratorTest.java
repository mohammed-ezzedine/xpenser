package me.ezzedine.mohammed.xpenser.core.account.opening;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AccountIdGeneratorTest {

    @Test
    @DisplayName("generates a non nullable id")
    void generates_a_non_nullable_id() {
        assertNotNull(new AccountIdGenerator().generate());
    }
}