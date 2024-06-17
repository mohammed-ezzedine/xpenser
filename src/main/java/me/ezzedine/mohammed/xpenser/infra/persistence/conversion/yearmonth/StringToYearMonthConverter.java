package me.ezzedine.mohammed.xpenser.infra.persistence.conversion.yearmonth;


import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Component
@ReadingConverter
public class StringToYearMonthConverter implements Converter<String, YearMonth> {
    @Override
    public YearMonth convert(@NonNull String source) {
        return YearMonth.parse(source);
    }
}
