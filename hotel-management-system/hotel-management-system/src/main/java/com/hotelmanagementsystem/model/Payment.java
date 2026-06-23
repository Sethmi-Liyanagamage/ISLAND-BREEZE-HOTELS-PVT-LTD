package com.hotelmanagementsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String paymentId;
    
    private String bookingId;
    private String guestId;
    private double amount;
    private String status; // PENDING, PAID, REFUNDED
    private String paymentMethod; // CREDIT_CARD, PAYPAL, etc.
    
    @Column(name = "payment_date")
    private LocalDateTime paymentDate;
}