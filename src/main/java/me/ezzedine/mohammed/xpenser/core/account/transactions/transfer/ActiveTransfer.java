package me.ezzedine.mohammed.xpenser.core.account.transactions.transfer;

import lombok.Builder;

@Builder
public record ActiveTransfer(
    String transactionId
) {
}
