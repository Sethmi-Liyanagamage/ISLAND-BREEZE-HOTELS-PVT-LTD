package com.hotelmanagementsystem.service;

import com.hotelmanagementsystem.events.PaymentSucceededEvent;
import com.hotelmanagementsystem.model.Booking;
import com.hotelmanagementsystem.model.Payment;
import com.hotelmanagementsystem.repository.BookingRepository;
import com.hotelmanagementsystem.repository.PaymentRepository;
import com.hotelmanagementsystem.service.payment.PaymentStrategy;
import com.hotelmanagementsystem.service.payment.PaymentStrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentStrategyFactory paymentStrategyFactory;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private BookingRepository bookingRepository;

    public Payment createPayment(Payment payment) {
        // If this is a new payment for a booking, set the amount from the booking
        if (payment.getBookingId() != null && payment.getAmount() == 0) {
            Booking booking = bookingRepository.findById(payment.getBookingId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + payment.getBookingId()));
            payment.setAmount(booking.getTotalAmount());
        }

        if (payment.getPaymentDate() == null) {
            payment.setPaymentDate(LocalDateTime.now());
        }
        if (payment.getStatus() == null || payment.getStatus().isEmpty()) {
            payment.setStatus("PENDING");
        }

        String type = payment.getPaymentMethod() != null ? payment.getPaymentMethod() : "CASH";
        PaymentStrategy strategy = paymentStrategyFactory.resolve(type);
        Payment processed = strategy.process(payment);
        Payment saved = paymentRepository.save(processed);

        if ("PAID".equalsIgnoreCase(saved.getStatus()) || "COMPLETED".equalsIgnoreCase(saved.getStatus())) {
            eventPublisher.publishEvent(new PaymentSucceededEvent(this, saved));
        }
        return saved;
    }
    
    public Payment createPaymentForBooking(String bookingId, String guestId, String paymentMethod) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + bookingId));
        
        // Check if a PENDING payment already exists for this booking
        List<Payment> existingPayments = paymentRepository.findByBookingId(bookingId);
        Payment pendingPayment = existingPayments.stream()
            .filter(p -> "PENDING".equalsIgnoreCase(p.getStatus()))
            .findFirst()
            .orElse(null);
        
        if (pendingPayment != null) {
            // Update the existing PENDING payment instead of creating a new one
            pendingPayment.setPaymentMethod(paymentMethod);
            pendingPayment.setPaymentDate(LocalDateTime.now());
            
            // Process the payment through strategy
            String type = paymentMethod != null ? paymentMethod : "CASH";
            PaymentStrategy strategy = paymentStrategyFactory.resolve(type);
            Payment processed = strategy.process(pendingPayment);
            Payment saved = paymentRepository.save(processed);
            
            if ("PAID".equalsIgnoreCase(saved.getStatus()) || "COMPLETED".equalsIgnoreCase(saved.getStatus())) {
                eventPublisher.publishEvent(new PaymentSucceededEvent(this, saved));
            }
            return saved;
        } else {
            // No pending payment exists, create a new one
            Payment payment = new Payment();
            payment.setBookingId(bookingId);
            payment.setGuestId(guestId);
            payment.setAmount(booking.getTotalAmount());
            payment.setPaymentMethod(paymentMethod);
            payment.setStatus("PENDING");
            
            return createPayment(payment);
        }
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> getPaymentById(String id) {
        return paymentRepository.findById(id);
    }

    public List<Payment> getPaymentsByGuestId(String guestId) {
        return paymentRepository.findByGuestId(guestId);
    }

    public List<Payment> getPaymentsByBookingId(String bookingId) {
        return paymentRepository.findByBookingId(bookingId);
    }
    
    public List<Payment> getPaymentsByStatus(String status) {
        return paymentRepository.findByStatus(status);
    }

    public Payment updatePayment(String id, Payment updatedPayment) {
        if (!paymentRepository.existsById(id)) {
            throw new IllegalArgumentException("Payment not found with id: " + id);
        }
        updatedPayment.setPaymentId(id);
        return paymentRepository.save(updatedPayment);
    }

    public void deletePayment(String id) {
        if (!paymentRepository.existsById(id)) {
            throw new IllegalArgumentException("Payment not found with id: " + id);
        }
        paymentRepository.deleteById(id);
    }
}


