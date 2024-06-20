package me.ezzedine.mohammed.xpenser.core.account;

import me.ezzedine.mohammed.xpenser.utils.AccountUtils;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class RegularAccountAggregateTest {
    private final AggregateTestFixture<RegularAccountAggregate> testFixture = new AggregateTestFixture<>(RegularAccountAggregate.class);

    @Test
    @DisplayName("it should throw an error upon receiving an open savings account command with a negative initial budget")
    void it_should_throw_an_error_upon_receiving_an_open_savings_account_command_with_a_negative_initial_budget() {
        testFixture.givenNoPriorActivity()
                .when(AccountUtils.openRegularAccountCommand().budgetInitialAmount(BigDecimal.valueOf(-3)).build())
                .expectException(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("it should publish an account opened event upon receiving an open savings account command")
    void it_should_publish_an_account_opened_event_upon_receiving_an_open_savings_account_command() {
        testFixture.givenNoPriorActivity()
                .when(AccountUtils.openRegularAccountCommand().build())
                .expectSuccessfulHandlerExecution()
                .expectEvents(AccountUtils.accountOpenedEvent());
    }

}