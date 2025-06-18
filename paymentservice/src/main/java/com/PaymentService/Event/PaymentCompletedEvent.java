package com.PaymentService.Event;


import org.springframework.context.ApplicationEvent;

public class PaymentCompletedEvent extends ApplicationEvent {
    private final String transactionId;

    public PaymentCompletedEvent(Object source, String transactionId) {
        super(source);
        this.transactionId = transactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }
}
