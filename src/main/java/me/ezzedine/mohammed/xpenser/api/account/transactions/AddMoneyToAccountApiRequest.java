package me.ezzedine.mohammed.xpenser.api.account.transactions;

import javax.annotation.Nonnull;

public record AddMoneyToAccountApiRequest(double amount, @Nonnull String note) {
}
