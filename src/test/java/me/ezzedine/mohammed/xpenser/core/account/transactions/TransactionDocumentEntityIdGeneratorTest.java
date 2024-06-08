package me.ezzedine.mohammed.xpenser.core.account.transactions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class TransactionDocumentEntityIdGeneratorTest {

    @Test
    @DisplayName("generates a non null id")
    void generates_a_non_null_id() {
        assertNotNull(new TransactionIdGenerator().generate().block());
    }
}