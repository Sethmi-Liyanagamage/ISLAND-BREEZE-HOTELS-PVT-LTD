package com.hotelmanagementsystem.controller;

import com.hotelmanagementsystem.model.Payment;
import com.hotelmanagementsystem.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {
    
    @Autowired
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody Payment payment) {
        try {
            Payment createdPayment = paymentService.createPayment(payment);
            return ResponseEntity.ok(createdPayment);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to create payment: " + e.getMessage());
        }
    }
    
    @PostMapping("/booking/{bookingId}")
    public ResponseEntity<?> createPaymentForBooking(
            @PathVariable String bookingId,
            @RequestParam String guestId,
            @RequestParam(required = false, defaultValue = "CASH") String paymentMethod) {
        try {
            Payment payment = paymentService.createPaymentForBooking(bookingId, guestId, paymentMethod);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to create payment for booking: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable String id) {
        return paymentService.getPaymentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/guest/{guestId}")
    public ResponseEntity<List<Payment>> getPaymentsByGuestId(
            @PathVariable String guestId,
            @RequestParam(required = false) String status) {
        
        if (status != null && !status.isEmpty()) {
            List<Payment> payments = paymentService.getPaymentsByGuestId(guestId)
                .stream()
                .filter(p -> status.equalsIgnoreCase(p.getStatus()))
                .toList();
            return ResponseEntity.ok(payments);
        }
        return ResponseEntity.ok(paymentService.getPaymentsByGuestId(guestId));
    }
    
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<Payment>> getPaymentsByBookingId(@PathVariable String bookingId) {
        return ResponseEntity.ok(paymentService.getPaymentsByBookingId(bookingId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePayment(@PathVariable String id, @RequestBody Payment payment) {
        try {
            Payment updatedPayment = paymentService.updatePayment(id, payment);
            return ResponseEntity.ok(updatedPayment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to update payment: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePayment(@PathVariable String id) {
        try {
            paymentService.deletePayment(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to delete payment: " + e.getMessage());
        }
    }
}