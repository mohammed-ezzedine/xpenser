package me.ezzedine.mohammed.xpenser.core.expense;

import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Service
public class YearMonthFactory {
    public YearMonth from(Date timestamp) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(timestamp);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        return YearMonth.of(year, month);
    }
}
