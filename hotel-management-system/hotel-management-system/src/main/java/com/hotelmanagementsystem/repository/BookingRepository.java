package com.hotelmanagementsystem.repository;

import com.hotelmanagementsystem.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {
    List<Booking> findByGuestId(String guestId);
    List<Booking> findByRoomId(String roomId);
    List<Booking> findByStatus(String status);
}


