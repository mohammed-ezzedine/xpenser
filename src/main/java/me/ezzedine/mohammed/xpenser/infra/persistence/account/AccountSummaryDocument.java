package me.ezzedine.mohammed.xpenser.infra.persistence.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Document(collection = "account_summary")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountSummaryDocument {
    private String id;
    private String name;
    private String currencyCode;
    private BigDecimal amount;
}
