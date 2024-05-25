package me.ezzedine.mohammed.xpenser.core.account.transactions;

public record MoneyDepositedInAccountEvent(
        String accountId,
        double amount
) {
}
