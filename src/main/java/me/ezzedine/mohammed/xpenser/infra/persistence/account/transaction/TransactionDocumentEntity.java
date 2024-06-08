package me.ezzedine.mohammed.xpenser.infra.persistence.account.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDocumentEntity {
    private BigDecimal amount;
    private BigDecimal balance;
    private String note;
    private Date timestamp;
}
