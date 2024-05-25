package me.ezzedine.mohammed.xpenser.api.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public class ResourceUtils {

    @SneakyThrows
    public static Object resource(String fileName) {
        return new ObjectMapper().readValue(ResourceUtils.class.getClassLoader().getResourceAsStream(fileName), Object.class);
    }
}
