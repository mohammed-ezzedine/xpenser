package me.ezzedine.mohammed.xpenser.infra.config.persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.List;

@Configuration
public class MongoConfiguration {

    @Bean
    public MongoCustomConversions customConversions(List<Converter<?, ?>> converters) {
        return new MongoCustomConversions(converters);
    }

}