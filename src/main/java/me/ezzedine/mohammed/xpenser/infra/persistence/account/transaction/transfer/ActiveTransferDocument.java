package me.ezzedine.mohammed.xpenser.infra.persistence.account.transaction.transfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "active_transfer")
public class ActiveTransferDocument {
    @Id
    private String transactionId;
}
