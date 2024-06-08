package me.ezzedine.mohammed.xpenser.utils;

import me.ezzedine.mohammed.xpenser.core.account.transactions.MoneyDepositedInAccountEvent;
import me.ezzedine.mohammed.xpenser.core.account.transactions.MoneyWithdrewFromAccountEvent;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import static org.mockito.Mockito.mock;

public class TransactionUtils {
    public static final String TRANSACTION_ID = UUID.randomUUID().toString();
    public static final BigDecimal TRANSACTION_AMOUNT = BigDecimal.valueOf(new Random().nextDouble(0, 100));
    public static final String TRANSACTION_NOTE = UUID.randomUUID().toString();
    private static final Date TRANSACTION_DATE = mock(Date.class);


    public static MoneyDepositedInAccountEvent moneyDepositedIntoAccountEvent() {
        return new MoneyDepositedInAccountEvent(TRANSACTION_ID, AccountUtils.ACCOUNT_ID, TRANSACTION_AMOUNT, TRANSACTION_NOTE, TRANSACTION_DATE);
    }

    public static MoneyWithdrewFromAccountEvent moneyWithdrewFromAccountEvent() {
        return new MoneyWithdrewFromAccountEvent(TRANSACTION_ID, AccountUtils.ACCOUNT_ID, TRANSACTION_AMOUNT, TRANSACTION_NOTE, TRANSACTION_DATE);
    }
}
