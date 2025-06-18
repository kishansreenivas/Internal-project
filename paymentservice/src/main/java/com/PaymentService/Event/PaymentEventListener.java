package com.PaymentService.Event;


import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventListener {

    @EventListener
    public void handlePaymentComplete(PaymentCompletedEvent event) {
        System.out.println("📬 Payment Event: Transaction Completed: " + event.getTransactionId());
    }
}
