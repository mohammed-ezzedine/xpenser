package me.ezzedine.mohammed.xpenser.core.expense;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.YearMonth;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class YearMonthFactoryTest {

    @Test
    @DisplayName("generates the correct year month from a date")
    void generates_the_correct_year_month_from_a_date() {
        Date timestamp = Date.from(Instant.parse("2024-06-18T09:07:52.535892800Z"));
        assertEquals(YearMonth.of(2024, 6), new YearMonthFactory().from(timestamp));
    }

}