package me.ezzedine.mohammed.xpenser.core.account;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ezzedine.mohammed.xpenser.core.account.budget.Budget;
import me.ezzedine.mohammed.xpenser.core.account.opening.AccountOpenedEvent;
import me.ezzedine.mohammed.xpenser.core.account.opening.OpenLoanAccountCommand;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.spring.stereotype.Aggregate;

import java.math.BigDecimal;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Slf4j
@Data
@Aggregate
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LoanAccountAggregate extends AccountAggregate {

    @CommandHandler
    public LoanAccountAggregate(OpenLoanAccountCommand command) {
        if (command.budgetInitialAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative.");
        }

        AccountOpenedEvent event = new AccountOpenedEvent(
                command.id(),
                command.name(),
                Budget.builder()
                        .currency(command.currencyCode())
                        .amount(command.budgetInitialAmount())
                        .build(),
                command.timestamp()
        );
        apply(event);
    }
}
