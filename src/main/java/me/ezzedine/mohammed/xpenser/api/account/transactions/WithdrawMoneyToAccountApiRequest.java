package me.ezzedine.mohammed.xpenser.api.account.transactions;

import javax.annotation.Nonnull;

public record WithdrawMoneyToAccountApiRequest(double amount, @Nonnull String note) {
}
