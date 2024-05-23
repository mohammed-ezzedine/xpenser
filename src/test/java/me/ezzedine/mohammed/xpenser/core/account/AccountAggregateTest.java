package me.ezzedine.mohammed.xpenser.core.account;

import me.ezzedine.mohammed.xpenser.core.account.opening.AccountOpenedEvent;
import me.ezzedine.mohammed.xpenser.core.account.opening.OpenAccountCommand;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class AccountAggregateTest {

    public static final String ACCOUNT_NAME = UUID.randomUUID().toString();
    public static final String ACCOUNT_ID = UUID.randomUUID().toString();
    private final AggregateTestFixture<AccountAggregate> testFixture = new AggregateTestFixture<>(AccountAggregate.class);

    @Test
    @DisplayName("it should publish an account opened event upon receiving an open account command")
    void it_should_publish_an_account_opened_event_upon_receiving_an_open_account_command() {
        testFixture.givenNoPriorActivity()
                .when(OpenAccountCommand.builder().name(ACCOUNT_NAME).id(ACCOUNT_ID).build())
                .expectSuccessfulHandlerExecution()
                .expectEvents(AccountOpenedEvent.builder().id(ACCOUNT_ID).name(ACCOUNT_NAME).build());
    }
}