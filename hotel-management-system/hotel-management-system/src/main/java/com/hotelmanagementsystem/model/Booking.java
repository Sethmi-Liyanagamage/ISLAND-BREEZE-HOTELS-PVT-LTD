package com.hotelmanagementsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String bookingId;

    private String guestId;
    private String roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double totalAmount;
    private int numberOfGuests;
    private String status; // PENDING, CONFIRMED, CANCELED, COMPLETED, PAID
    private String guestName;
    private String roomDetails; // e.g., "Deluxe Suite - #101"
    private String createdAt;
}