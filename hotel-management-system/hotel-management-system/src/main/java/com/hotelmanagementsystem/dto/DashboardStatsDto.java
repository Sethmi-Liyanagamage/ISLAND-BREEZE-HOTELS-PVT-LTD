package com.hotelmanagementsystem.dto;

import lombok.Data;

@Data
public class DashboardStatsDto {
    private long totalGuests;
    private long totalRooms;
    private long availableRooms;
    private long totalBookings;
    private double totalRevenue;
}