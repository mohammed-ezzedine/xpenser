package me.ezzedine.mohammed.xpenser.core.account.summary;

import me.ezzedine.mohammed.xpenser.core.account.opening.AccountOpenedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountSummaryProjectionMapper {

    @Mapping(target = "budget.currencyCode", source = "budget.currency")
    @Mapping(target = "budget.amount", source = "budget.amount")
    AccountSummary map(AccountOpenedEvent event);
}
