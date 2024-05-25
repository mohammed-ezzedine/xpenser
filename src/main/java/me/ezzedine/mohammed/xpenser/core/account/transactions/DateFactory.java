package me.ezzedine.mohammed.xpenser.core.account.transactions;

import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DateFactory {

    public Date now() {
        return new Date();
    }
}
