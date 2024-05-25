package me.ezzedine.mohammed.xpenser.core.account;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ezzedine.mohammed.xpenser.core.account.budget.Budget;
import me.ezzedine.mohammed.xpenser.core.account.budget.Currencies;
import me.ezzedine.mohammed.xpenser.core.account.opening.AccountOpenedEvent;
import me.ezzedine.mohammed.xpenser.core.account.opening.OpenAccountCommand;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Slf4j
@Aggregate
@NoArgsConstructor
public class AccountAggregate {

    @AggregateIdentifier
    private String id;
    private String name;
    private Budget budget;

    @CommandHandler
    public AccountAggregate(OpenAccountCommand command) {
        log.info("A new account opened.");
        AccountOpenedEvent event = new AccountOpenedEvent(
                command.getId(),
                command.getName(),
                Budget.builder()
                        .currency(Currencies.fromCode(command.getCurrencyCode()))
                        .amount(command.getBudgetInitialAmount())
                        .build()
        );
        apply(event);
    }

    @EventSourcingHandler
    public void on(AccountOpenedEvent event) {
        this.id = event.id();
        this.name = event.name();
        this.budget = event.budget();
    }
}
