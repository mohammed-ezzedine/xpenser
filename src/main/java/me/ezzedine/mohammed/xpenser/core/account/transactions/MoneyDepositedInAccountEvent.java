package me.ezzedine.mohammed.xpenser.core.account.transactions;

import java.util.Date;

public record MoneyDepositedInAccountEvent(
        String accountId,
        double amount,
        Date timestamp
) {
}
