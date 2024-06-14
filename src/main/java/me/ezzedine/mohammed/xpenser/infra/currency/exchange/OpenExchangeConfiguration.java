package me.ezzedine.mohammed.xpenser.infra.currency.exchange;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "xpenser.currency.exchange")
public class OpenExchangeConfiguration {

    private String appId;
    private String baseUri;
}
