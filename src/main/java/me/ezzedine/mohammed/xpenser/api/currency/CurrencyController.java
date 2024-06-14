package me.ezzedine.mohammed.xpenser.api.currency;

import me.ezzedine.mohammed.xpenser.core.currency.CurrencyCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Arrays;

@RestController
@RequestMapping("currencies")
public class CurrencyController {

    @GetMapping
    Flux<CurrencyCode> fetchListOfSupportedCurrencies() {
        return Flux.fromStream(Arrays.stream(CurrencyCode.values()));
    }
}
