package me.ezzedine.mohammed.xpenser.api.expense.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CreateExpenseCategoryApiRequest(
    @NotBlank String name,
    @NotBlank String icon
) {
}
