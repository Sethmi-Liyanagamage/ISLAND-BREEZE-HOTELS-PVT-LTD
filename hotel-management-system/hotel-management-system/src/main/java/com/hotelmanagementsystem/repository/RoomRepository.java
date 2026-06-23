package com.hotelmanagementsystem.repository;

import com.hotelmanagementsystem.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, String> {
    List<Room> findByStatusIgnoreCase(String status);
    List<Room> findByTypeIgnoreCase(String type);
}


