package me.ezzedine.mohammed.xpenser.infra.persistence.account.transaction;

import me.ezzedine.mohammed.xpenser.core.account.transactions.query.AccountTransactionSummary;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountTransactionsMapper {

    AccountTransactionsDocument map(AccountTransactionSummary summary);
    AccountTransactionSummary map(AccountTransactionsDocument summary);
}
