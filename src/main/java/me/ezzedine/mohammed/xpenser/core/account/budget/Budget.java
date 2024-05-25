package me.ezzedine.mohammed.xpenser.core.account.budget;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Budget {
    private Currency currency;
    private double amount;
}
