package me.ezzedine.mohammed.xpenser.infra.persistence.account;

import me.ezzedine.mohammed.xpenser.core.account.query.AccountSummary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountSummaryDocumentMapper {

    @Mapping(target = "currencyCode", source = "budget.currencyCode")
    @Mapping(target = "amount", source = "budget.amount")
    AccountSummaryDocument map(AccountSummary accountSummary);


    @Mapping(target = "budget.currencyCode", source = "currencyCode")
    @Mapping(target = "budget.amount", source = "amount")
    AccountSummary map(AccountSummaryDocument accountSummary);
}
