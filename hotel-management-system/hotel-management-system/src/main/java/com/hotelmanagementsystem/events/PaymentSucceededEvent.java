package com.hotelmanagementsystem.events;

import com.hotelmanagementsystem.model.Payment;
import org.springframework.context.ApplicationEvent;

public class PaymentSucceededEvent extends ApplicationEvent {
    private final Payment payment;

    public PaymentSucceededEvent(Object source, Payment payment) {
        super(source);
        this.payment = payment;
    }

    public Payment getPayment() {
        return payment;
    }
}


