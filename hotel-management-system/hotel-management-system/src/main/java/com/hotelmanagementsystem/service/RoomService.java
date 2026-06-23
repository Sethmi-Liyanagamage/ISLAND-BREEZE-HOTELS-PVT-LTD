package com.hotelmanagementsystem.service;

import com.hotelmanagementsystem.model.Room;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.hotelmanagementsystem.repository.RoomRepository;

import java.util.*;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room createRoom(Room room) {
        if (room.getId() == null || room.getId().isEmpty()) {
            room.setId(generateShortId());
        }
        if (room.getStatus() == null || room.getStatus().isEmpty()) {
            room.setStatus("AVAILABLE");
        } else {
            room.setStatus(room.getStatus().toUpperCase());
        }
        return roomRepository.save(room);
    }

    public Room updateRoom(String id, Room updatedRoom) {
        if (!roomRepository.existsById(id)) {
            throw new RuntimeException("Room not found");
        }
        updatedRoom.setId(id);
        if (updatedRoom.getStatus() != null) {
            updatedRoom.setStatus(updatedRoom.getStatus().toUpperCase());
        }
        return roomRepository.save(updatedRoom);
    }

    public boolean deleteRoom(String id) {
        if (!roomRepository.existsById(id)) {
            return false;
        }
        roomRepository.deleteById(id);
        return true;
    }

    public List<Room> getAvailableRooms() {
        return roomRepository.findByStatusIgnoreCase("AVAILABLE");
    }

    public List<Room> getRoomsByType(String type) {
        return roomRepository.findByTypeIgnoreCase(type);
    }
    
    public Optional<Room> getRoomById(String id) {
        return roomRepository.findById(id);
    }

    private String generateShortId() {
        String id;
        do {
            id = "RM" + randomAlnum(6);
        } while (roomRepository.existsById(id));
        return id;
    }

    private String randomAlnum(int len) {
        final String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"; // no confusing chars
        StringBuilder sb = new StringBuilder(len);
        Random rnd = new Random();
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
