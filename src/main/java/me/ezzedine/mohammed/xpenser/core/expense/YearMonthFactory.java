package me.ezzedine.mohammed.xpenser.core.expense;

import org.springframework.stereotype.Service;

import java.time.YearMonth;

@Service
public class YearMonthFactory {
    public YearMonth now() {
        return YearMonth.now();
    }
}
