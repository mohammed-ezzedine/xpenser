package me.ezzedine.mohammed.xpenser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class XpenserApplication {

    public static void main(String[] args) {
        SpringApplication.run(XpenserApplication.class, args);
    }

    /*
     public CommandBus myBus(SpanFactory sf) {
        return AxonServerCommandBus.builder()
            .spanFactory(sf)
             // Other configuration
            .build()
    }
* */
}
