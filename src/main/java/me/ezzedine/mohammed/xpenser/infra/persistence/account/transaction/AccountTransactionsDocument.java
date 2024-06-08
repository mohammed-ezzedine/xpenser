package me.ezzedine.mohammed.xpenser.infra.persistence.account.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "account_transactions")
public class AccountTransactionsDocument {
    private String id;
    private List<TransactionDocumentEntity> transactions = new ArrayList<>();
}
