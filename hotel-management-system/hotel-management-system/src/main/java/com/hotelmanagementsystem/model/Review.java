package com.hotelmanagementsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String reviewId;
    
    private String bookingId;
    private String guestId;
    private int rating; // 1-5
    
    @Column(length = 2000)
    private String comment;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    private String status; // PENDING, APPROVED, REJECTED
    
    @Column(length = 1000)
    private String adminReply;
}