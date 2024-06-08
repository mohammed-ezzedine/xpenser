package me.ezzedine.mohammed.xpenser.core.account.transactions.query;

import me.ezzedine.mohammed.xpenser.core.account.opening.AccountOpenedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountTransactionMapper {

    @Mapping(target = "amount", source = "budget.amount")
    @Mapping(target = "balance", source = "budget.amount")
    @Mapping(target = "note", constant = "Account opened.")
    TransactionSummary map(AccountOpenedEvent event);
}
