package me.ezzedine.mohammed.xpenser.core.account;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ezzedine.mohammed.xpenser.core.account.budget.Budget;
import me.ezzedine.mohammed.xpenser.core.account.opening.AccountOpenedEvent;
import me.ezzedine.mohammed.xpenser.core.account.opening.OpenAccountCommand;
import me.ezzedine.mohammed.xpenser.core.account.transactions.*;
import me.ezzedine.mohammed.xpenser.core.currency.exchange.CurrencyExchangeManager;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.math.BigDecimal;
import java.util.Optional;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Slf4j
@Data
@Aggregate
@NoArgsConstructor
public class AccountAggregate {

    @AggregateIdentifier
    private String id;
    private String name;
    private Budget budget;

    @CommandHandler
    public AccountAggregate(OpenAccountCommand command) {
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

    @EventSourcingHandler
    public void on(AccountOpenedEvent event) {
        this.id = event.id();
        this.name = event.name();
        this.budget = event.budget();
    }

    @CommandHandler
    public void handle(DepositMoneyCommand command, CurrencyExchangeManager currencyExchangeManager) {
        validateAmountIsGreaterThanZero(command.amount());

        BigDecimal amount = Optional.ofNullable(command.currencyCode())
                .map(currency -> currencyExchangeManager.convert(command.amount(), currency, budget.getCurrency()))
                .orElse(command.amount());

        MoneyDepositedInAccountEvent event = MoneyDepositedInAccountEvent.builder()
                .transactionId(command.transactionId())
                .accountId(command.accountId())
                .amount(amount)
                .note(command.note())
                .timestamp(command.timestamp())
                .build();
        AggregateLifecycle.apply(event);
    }
    @EventSourcingHandler
    public void on(MoneyDepositedInAccountEvent event) {
        this.budget = new Budget(budget.getCurrency(), budget.getAmount().add(event.amount()));
    }

    @CommandHandler
    public void handle(WithdrawMoneyCommand command) {
        validateAmountIsGreaterThanZero(command.amount());
        validateAmountCanBeWithdrewFromBudget(command.amount());

        apply(new MoneyWithdrewFromAccountEvent(command.transactionId(), command.accountId(), command.amount(), budget.getCurrency(),
                command.note(), command.timestamp()));
    }

    @EventSourcingHandler
    public void on(MoneyWithdrewFromAccountEvent event) {
        this.budget = new Budget(budget.getCurrency(), budget.getAmount().subtract(event.amount()));
    }

    @CommandHandler
    public void handle(TransferMoneyCommand command) {
        validateAmountIsGreaterThanZero(command.amount());
        validateAmountCanBeWithdrewFromBudget(command.amount());

        apply(new MoneyTransferInitiatedEvent(command.transactionId(), command.sourceAccountId(), command.destinationAccountId(), command.amount(), command.timestamp()));
    }

    private void validateAmountCanBeWithdrewFromBudget(BigDecimal amount) {
        if (!this.budget.canWithdraw(amount)) {
            throw new IllegalArgumentException("Not enough balance to transfer");
        }
    }

    private static void validateAmountIsGreaterThanZero(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount should be greater than zero.");
        }
    }

}
