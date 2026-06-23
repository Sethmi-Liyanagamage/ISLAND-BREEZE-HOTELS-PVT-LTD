package com.hotelmanagementsystem.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "rooms")
public class Room {
    @Id
    private String id;

    @Column(unique = true, nullable = false)
    private String roomNumber;
    
    private String type; // e.g., "Deluxe Suite", "Standard Room"
    
    @Column(name = "price_per_night")
    private double pricePerNight;
    
    private int capacity; // Number of guests
    
    private String status; // AVAILABLE, OCCUPIED, MAINTENANCE
    
    @Column(length = 1000)
    private String description;
}