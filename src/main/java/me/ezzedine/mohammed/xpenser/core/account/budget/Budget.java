package me.ezzedine.mohammed.xpenser.core.account.budget;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Budget {
    private Currency currency;
    private BigDecimal amount;

    public boolean canWithdraw(BigDecimal amount) {
        return this.amount.compareTo(amount) >= 0;
    }
}
