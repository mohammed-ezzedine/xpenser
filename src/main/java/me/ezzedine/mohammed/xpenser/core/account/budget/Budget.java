package me.ezzedine.mohammed.xpenser.core.account.budget;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Budget {
    private Currency currency;
    private double amount;

    public boolean canWithdraw(double amount) {
        return this.amount >= amount;
    }
}
