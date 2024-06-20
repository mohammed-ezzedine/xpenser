package me.ezzedine.mohammed.xpenser.core.account;

import me.ezzedine.mohammed.xpenser.utils.AccountUtils;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class InvestmentsAccountAggregateTest {

    private final AggregateTestFixture<InvestmentsAccountAggregate> testFixture = new AggregateTestFixture<>(InvestmentsAccountAggregate.class);

    @Test
    @DisplayName("it should throw an error upon receiving an open investments account command with a negative initial budget")
    void it_should_throw_an_error_upon_receiving_an_open_investments_account_command_with_a_negative_initial_budget() {
        testFixture.givenNoPriorActivity()
                .when(AccountUtils.openInvestmentsAccountCommand().budgetInitialAmount(BigDecimal.valueOf(-3)).build())
                .expectException(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("it should publish an account opened event upon receiving an open investments account command")
    void it_should_publish_an_account_opened_event_upon_receiving_an_open_investments_account_command() {
        testFixture.givenNoPriorActivity()
                .when(AccountUtils.openInvestmentsAccountCommand().build())
                .expectSuccessfulHandlerExecution()
                .expectEvents(AccountUtils.accountOpenedEvent());
    }

}