package com.hotelmanagementsystem.repository;

import com.hotelmanagementsystem.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    List<Payment> findByGuestId(String guestId);
    List<Payment> findByBookingId(String bookingId);
    List<Payment> findByStatus(String status);
}
