package me.ezzedine.mohammed.xpenser.infra.persistence.conversion.yearmonth;


import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Component
@WritingConverter
public class YearMonthToStringConverter implements Converter<YearMonth, String> {
    @Override
    public String convert(@NonNull YearMonth source) {
        return source.toString();
    }
}
