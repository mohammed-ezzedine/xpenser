package me.ezzedine.mohammed.xpenser.core.expense;

import me.ezzedine.mohammed.xpenser.utils.ExpenseCategoryUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ExpenseCategoryServiceTest {

    private ExpenseCategoryStorage storage;
    private ExpenseCategoryService expenseCategoryService;

    @BeforeEach
    void setUp() {
        storage = mock(ExpenseCategoryStorage.class);
        ExpenseCategoryIdGenerator idGenerator = mock(ExpenseCategoryIdGenerator.class);
        expenseCategoryService = new ExpenseCategoryService(storage, idGenerator, new ExpenseCategoryMapperImpl());

        when(idGenerator.generate()).thenReturn(Mono.just(ExpenseCategoryUtils.EXPENSE_CATEGORY_ID));
    }

    @Test
    @DisplayName("it should save a new category in the storage upon receiving a request to create a new expense category")
    void it_should_save_a_new_category_in_the_storage_upon_receiving_a_request_to_create_a_new_expense_category() {
        when(storage.save(any())).thenReturn(Mono.empty());

        Mono<ExpenseCategory> mono = expenseCategoryService.create(ExpenseCategoryUtils.createExpenseCategoryRequest().build());
        StepVerifier.create(mono)
                .expectNext(ExpenseCategoryUtils.expenseCategory().build())
                .expectComplete()
                .verify();

        verify(storage).save(ExpenseCategoryUtils.expenseCategory().build());
    }

    @Test
    @DisplayName("it should read and return the list of expense categories from the storage upon receiving to fetch all categories")
    void it_should_read_and_return_the_list_of_expense_categories_from_the_storage_upon_receiving_to_fetch_all_categories() {
        when(storage.fetchAll()).thenReturn(Flux.just(ExpenseCategoryUtils.expenseCategory().build()));

        Flux<ExpenseCategory> flux = expenseCategoryService.fetchAllCategories();
        StepVerifier.create(flux)
                .expectNext(ExpenseCategoryUtils.expenseCategory().build())
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("it should fetch the category form the storage upon receiving a request to fetch a storage by id")
    void it_should_fetch_the_category_form_the_storage_upon_receiving_a_request_to_fetch_a_storage_by_id() {
        when(storage.fetch(ExpenseCategoryUtils.EXPENSE_CATEGORY_ID)).thenReturn(Mono.just(ExpenseCategoryUtils.expenseCategory().build()));

        Mono<ExpenseCategory> mono = expenseCategoryService.fetch(ExpenseCategoryUtils.EXPENSE_CATEGORY_ID);
        StepVerifier.create(mono)
                .expectNext(ExpenseCategoryUtils.expenseCategory().build())
                .verifyComplete();
    }
}