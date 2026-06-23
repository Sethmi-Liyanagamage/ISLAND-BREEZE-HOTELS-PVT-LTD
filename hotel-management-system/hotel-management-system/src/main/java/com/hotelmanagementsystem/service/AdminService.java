package com.hotelmanagementsystem.service;

import com.hotelmanagementsystem.model.Admin;
import com.hotelmanagementsystem.dto.AdminRegistrationDto;
import com.hotelmanagementsystem.dto.AdminLoginDto;
import com.hotelmanagementsystem.dto.DashboardStatsDto;
import com.hotelmanagementsystem.dto.AdminProfileUpdateDto;
import com.hotelmanagementsystem.repository.UserRepository;
import com.hotelmanagementsystem.repository.RoomRepository;
import com.hotelmanagementsystem.repository.BookingRepository;
import com.hotelmanagementsystem.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.hotelmanagementsystem.repository.AdminRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoomRepository roomRepository;
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private PaymentRepository paymentRepository;

    public Admin registerAdmin(AdminRegistrationDto dto) {
        // Check if username already exists
        if (adminRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        
        Admin admin = new Admin();
        admin.setFullName(dto.getFullName());
        admin.setEmail(dto.getEmail());
        admin.setUsername(dto.getUsername());
        admin.setPassword(dto.getPassword()); // In production, hash this password
        admin.setRole(dto.getRole());
        admin.setCreatedAt(LocalDateTime.now().toString());

        return adminRepository.save(admin);
    }

    public Admin loginAdmin(AdminLoginDto loginDto) {
        return adminRepository.findByUsername(loginDto.getUsername())
            .filter(admin -> admin.getPassword().equals(loginDto.getPassword()))
            .orElse(null);
    }

    public DashboardStatsDto getDashboardStats() {
        DashboardStatsDto stats = new DashboardStatsDto();
        
        // Count guests (users with GUEST role)
        stats.setTotalGuests(userRepository.findByRole("GUEST").size());
        
        // Count total rooms
        stats.setTotalRooms(roomRepository.count());
        
        // Count available rooms (status = AVAILABLE)
        long availableRooms = roomRepository.findByStatusIgnoreCase("AVAILABLE").size();
        stats.setAvailableRooms(availableRooms);
        
        // Count total bookings
        stats.setTotalBookings(bookingRepository.count());
        
        // Calculate total revenue from completed/paid payments
        double totalRevenue = 0.0;
        try {
            totalRevenue += paymentRepository.findByStatus("COMPLETED")
                .stream()
                .mapToDouble(payment -> payment.getAmount())
                .sum();
            totalRevenue += paymentRepository.findByStatus("PAID")
                .stream()
                .mapToDouble(payment -> payment.getAmount())
                .sum();
        } catch (Exception e) {
            // If query fails, revenue stays 0
        }
        stats.setTotalRevenue(totalRevenue);
        
        return stats;
    }
    
    public List<String> getRecentActivity() {
        // For now, return a simple list. In production, you'd have an Activity table
        List<String> activities = new ArrayList<>();
        activities.add(LocalDateTime.now() + " - System activity logged");
        return activities;
    }

    public Admin updateAdminProfile(AdminProfileUpdateDto updateDto) {
        Admin admin = adminRepository.findById(updateDto.getId())
            .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (updateDto.getFullName() != null) {
            admin.setFullName(updateDto.getFullName());
        }
        if (updateDto.getEmail() != null) {
            admin.setEmail(updateDto.getEmail());
        }
        if (updateDto.getPassword() != null) {
            admin.setPassword(updateDto.getPassword());
        }

        return adminRepository.save(admin);
    }
    
    public Optional<Admin> getAdminById(String id) {
        return adminRepository.findById(id);
    }
    
    public Optional<Admin> getAdminByUsername(String username) {
        return adminRepository.findByUsername(username);
    }
}