package com.hotelmanagementsystem.service.payment;

import com.hotelmanagementsystem.model.Payment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Strategy for creating pending payments. These payments are not yet processed
 * and will be updated to PAID when the actual payment is made.
 */
@Component
public class PendingPaymentStrategy implements PaymentStrategy {

    @Override
    public Payment process(Payment payment) {
        // Set default values for pending payment
        if (payment.getPaymentDate() == null) {
            payment.setPaymentDate(LocalDateTime.now());
        }
        payment.setStatus("PENDING");
        if (payment.getPaymentMethod() == null || payment.getPaymentMethod().isEmpty()) {
            payment.setPaymentMethod("PENDING");
        }
        return payment;
    }

    @Override
    public String getType() {
        return "PENDING";
    }
}
