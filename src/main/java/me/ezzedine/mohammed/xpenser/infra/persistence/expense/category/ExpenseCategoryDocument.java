package me.ezzedine.mohammed.xpenser.infra.persistence.expense.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "expense_category")
public class ExpenseCategoryDocument {
    private String id;
    private String name;
    private String icon;
}
