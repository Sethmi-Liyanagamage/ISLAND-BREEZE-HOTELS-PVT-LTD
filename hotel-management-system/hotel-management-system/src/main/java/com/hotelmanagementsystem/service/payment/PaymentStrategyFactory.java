package com.hotelmanagementsystem.service.payment;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PaymentStrategyFactory {

    private final Map<String, PaymentStrategy> typeToStrategy;

    public PaymentStrategyFactory(Map<String, PaymentStrategy> strategiesByName) {
        // Spring injects beans by name; we'll remap by type key for readability
        this.typeToStrategy = strategiesByName.values().stream()
                .collect(java.util.stream.Collectors.toMap(PaymentStrategy::getType, s -> s, (a, b) -> a));
    }

    public PaymentStrategy resolve(String type) {
        if (type == null || type.isEmpty()) {
            type = "CARD"; // default
        }
        PaymentStrategy strategy = typeToStrategy.get(type);
        if (strategy == null) {
            // Fallback to default card processing
            strategy = typeToStrategy.get("CARD");
        }
        return strategy;
    }
}


