package me.ezzedine.mohammed.xpenser.api.currency;

import me.ezzedine.mohammed.xpenser.api.account.ResourceUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(CurrencyController.class)
class CurrencyControllerTest {

    @Autowired
    private WebTestClient client;

    @Test
    @DisplayName("it should return the list of supported currencies")
    void it_should_return_the_list_of_supported_currencies() {
        client.get()
                .uri("/currencies")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .json(ResourceUtils.resourceAsString("currency/api/query/fetch_all_response.json"));
    }
}