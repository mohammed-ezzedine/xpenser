package me.ezzedine.mohammed.xpenser.core.account.transactions;

import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;

@Saga
@NoArgsConstructor
public class TransferMoneySaga {

    @Autowired
    private transient CommandGateway commandGateway;

    private String transactionId;
    private String sourceAccountId;
    private String destinationAccountId;
    private BigDecimal amount;
    private Date timestamp;

    @StartSaga
    @SagaEventHandler(associationProperty = "transactionId")
    public void on(MoneyTransferInitiatedEvent event) {
        transactionId = event.transactionId();
        sourceAccountId = event.sourceAccountId();
        destinationAccountId = event.destinationAccountId();
        amount = event.amount();
        timestamp = event.timestamp();

        this.commandGateway.send(new WithdrawMoneyCommand(transactionId, sourceAccountId, amount, "Internal Transfer", timestamp));
    }

    @SagaEventHandler(associationProperty = "transactionId")
    public void on(MoneyWithdrewFromAccountEvent event) {
        this.commandGateway.send(new DepositMoneyCommand(transactionId, destinationAccountId, amount, "Internal Transfer", timestamp));
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "transactionId")
    public void on(MoneyDepositedInAccountEvent event) {

    }
}
