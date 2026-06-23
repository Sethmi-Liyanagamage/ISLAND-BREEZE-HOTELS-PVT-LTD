package com.hotelmanagementsystem.service.payment;

import com.hotelmanagementsystem.model.Payment;

/**
 * Strategy for processing payments. Implementations should set status and any gateway details
 * on the provided Payment and return it. Implementations must be stateless and thread-safe.
 */
public interface PaymentStrategy {

    /**
     * Processes the provided payment and returns the updated instance.
     */
    Payment process(Payment payment);

    /**
     * A unique type key used to resolve this strategy (e.g., "CARD", "PAYPAL").
     */
    String getType();
}


