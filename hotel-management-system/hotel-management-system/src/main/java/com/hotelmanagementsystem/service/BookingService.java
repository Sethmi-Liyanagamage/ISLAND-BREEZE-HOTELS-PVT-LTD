package com.hotelmanagementsystem.service;

import com.hotelmanagementsystem.events.BookingCreatedEvent;
import com.hotelmanagementsystem.model.Booking;
import com.hotelmanagementsystem.model.Payment;
import com.hotelmanagementsystem.service.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.hotelmanagementsystem.repository.BookingRepository;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    @Autowired
    private PaymentService paymentService;

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking createBooking(Booking booking) {
        if (booking.getCreatedAt() == null || booking.getCreatedAt().isEmpty()) {
            booking.setCreatedAt(LocalDateTime.now().toString());
        }
        if (booking.getStatus() == null || booking.getStatus().isEmpty()) {
            booking.setStatus("PENDING");
        }
        Booking saved = bookingRepository.save(booking);
        
        // Create a pending payment for this booking
        try {
            Payment payment = new Payment();
            payment.setBookingId(saved.getBookingId());
            payment.setGuestId(saved.getGuestId());
            payment.setAmount(saved.getTotalAmount());
            payment.setPaymentMethod("PENDING"); // This will use the PendingPaymentStrategy
            paymentService.createPayment(payment);
        } catch (Exception e) {
            // Log the error but don't fail the booking creation
            System.err.println("Failed to create pending payment for booking: " + e.getMessage());
        }
        
        eventPublisher.publishEvent(new BookingCreatedEvent(this, saved));
        return saved;
    }

    public Booking updateBooking(String id, Booking updatedBooking) {
        if (!bookingRepository.existsById(id)) {
            throw new RuntimeException("Booking not found");
        }
        updatedBooking.setBookingId(id);
        return bookingRepository.save(updatedBooking);
    }

    public boolean cancelBooking(String id) {
        if (!bookingRepository.existsById(id)) {
            return false;
        }
        bookingRepository.deleteById(id);
        return true;
    }

    public List<Booking> getBookingsByGuestId(String guestId) {
        return bookingRepository.findByGuestId(guestId);
    }

    public List<Booking> getBookingsByRoomId(String roomId) {
        return bookingRepository.findByRoomId(roomId);
    }
    
    public Optional<Booking> getBookingById(String id) {
        return bookingRepository.findById(id);
    }
    
    public List<Booking> getBookingsByStatus(String status) {
        return bookingRepository.findByStatus(status);
    }
}