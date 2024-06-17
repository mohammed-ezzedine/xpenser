package me.ezzedine.mohammed.xpenser.api.expense.category;

import lombok.RequiredArgsConstructor;
import me.ezzedine.mohammed.xpenser.core.expense.category.ExpenseCategoryService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("expenses/categories")
@RequiredArgsConstructor
public class ExpenseCategoryController {

    private final ExpenseCategoryService expenseCategoryService;
    private final ExpenseCategoryApiMapper mapper;

    @GetMapping
    public Flux<ExpenseCategoryApiModel> fetchAllCategories() {
        return expenseCategoryService.fetchAllCategories().map(mapper::map);
    }

    @PostMapping
    public Mono<ExpenseCategoryApiModel> create(@RequestBody CreateExpenseCategoryApiRequest request) {
        return expenseCategoryService.create(mapper.map(request))
                .map(mapper::map);
    }
}
