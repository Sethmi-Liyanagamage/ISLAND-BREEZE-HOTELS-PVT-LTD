package com.hotelmanagementsystem.dto;

import lombok.Data;

@Data
public class CardDto {
    private String cardId;
    private String userId;
    private String cardNumber; // Will be masked in responses (e.g., ****1234)
    private String cardHolderName;
    private String expiryDate; // MM/YY format
    private String cardType; // CREDIT, DEBIT
    private boolean isDefault;
    private String createdAt;
    private String updatedAt;
}
