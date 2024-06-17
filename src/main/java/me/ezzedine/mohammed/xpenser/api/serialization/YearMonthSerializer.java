package me.ezzedine.mohammed.xpenser.api.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.YearMonth;

@Component
public class YearMonthSerializer extends StdSerializer<YearMonth> {
    protected YearMonthSerializer() {
        super(YearMonth.class);
    }

    @Override
    public void serialize(YearMonth yearMonth, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(yearMonth.toString());
    }
}
