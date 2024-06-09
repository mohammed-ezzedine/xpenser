package me.ezzedine.mohammed.xpenser.core.account;

import me.ezzedine.mohammed.xpenser.core.exception.NotFoundException;

public class AccountNotFoundException extends NotFoundException {

    public AccountNotFoundException(String id) {
        super("Account '%s' does not exist.".formatted(id));
    }
}
