package me.ezzedine.mohammed.xpenser.api.expense.category;

import me.ezzedine.mohammed.xpenser.api.account.ResourceUtils;
import me.ezzedine.mohammed.xpenser.core.expense.category.ExpenseCategoryService;
import me.ezzedine.mohammed.xpenser.utils.ExpenseCategoryUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {ExpenseCategoryApiMapperImpl.class, ExpenseCategoryController.class})
@WebFluxTest(controllers = ExpenseCategoryController.class)
class ExpenseCategoryControllerIntegrationTest {

    @MockBean
    private ExpenseCategoryService service;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("it should return the list of existing categories correctly")
    void it_should_return_the_list_of_existing_categories_correctly() {
        when(service.fetchAllCategories()).thenReturn(Flux.just(ExpenseCategoryUtils.expenseCategory().build()));

        webTestClient.get()
                .uri("/expenses/categories")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .json(ResourceUtils.resourceAsString("expense/category/api/fetch_all_response.json")
                        .replace("{ID}", ExpenseCategoryUtils.EXPENSE_CATEGORY_ID)
                        .replace("{NAME}", ExpenseCategoryUtils.EXPENSE_CATEGORY_NAME)
                        .replace("{ICON}", ExpenseCategoryUtils.EXPENSE_CATEGORY_ICON)
                );
    }

    @Test
    @DisplayName("it should create a new category and return its result correctly")
    void it_should_create_a_new_category_and_return_its_result_correctly() {
        when(service.create(ExpenseCategoryUtils.createExpenseCategoryRequest().build())).thenReturn(Mono.just(ExpenseCategoryUtils.expenseCategory().build()));

        webTestClient.post()
                .uri("/expenses/categories")
                .bodyValue(ExpenseCategoryUtils.createExpenseCategoryApiRequest().build())
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .json(ResourceUtils.resourceAsString("expense/category/api/expense_category.json")
                        .replace("{ID}", ExpenseCategoryUtils.EXPENSE_CATEGORY_ID)
                        .replace("{NAME}", ExpenseCategoryUtils.EXPENSE_CATEGORY_NAME)
                        .replace("{ICON}", ExpenseCategoryUtils.EXPENSE_CATEGORY_ICON)
                );
    }

}