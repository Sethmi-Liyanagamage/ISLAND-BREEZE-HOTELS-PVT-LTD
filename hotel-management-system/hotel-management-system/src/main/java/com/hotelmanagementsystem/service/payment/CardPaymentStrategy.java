package com.hotelmanagementsystem.service.payment;

import com.hotelmanagementsystem.model.Payment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CardPaymentStrategy implements PaymentStrategy {

    @Override
    public Payment process(Payment payment) {
        // Simulate card processing success. In real life, call the gateway here.
        if (payment.getPaymentDate() == null) {
            payment.setPaymentDate(LocalDateTime.now());
        }
        payment.setStatus("PAID");
        if (payment.getPaymentMethod() == null || payment.getPaymentMethod().isEmpty()) {
            payment.setPaymentMethod("CARD");
        }
        return payment;
    }

    @Override
    public String getType() {
        return "CARD";
    }
}


