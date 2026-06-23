package com.hotelmanagementsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String cardId;
    
    @Column(nullable = false)
    private String userId; // Foreign key to User
    
    @Column(nullable = false, length = 12)
    private String cardNumber; // 12 digits as requested
    
    @Column(nullable = false)
    private String cardHolderName;
    
    @Column(nullable = false, length = 3)
    private String cvv; // 3 digit CVV
    
    @Column(nullable = false, length = 5)
    private String expiryDate; // MM/YY format
    
    @Column(nullable = false)
    private String cardType; // CREDIT, DEBIT
    
    private boolean isDefault; // Whether this is the default card for the user
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
