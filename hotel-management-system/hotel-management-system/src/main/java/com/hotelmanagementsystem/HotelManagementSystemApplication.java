package com.hotelmanagementsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({
    "com.hotelmanagementsystem.controller",
    "com.hotelmanagementsystem.service",
    "com.hotelmanagementsystem.repository",
    "com.hotelmanagementsystem.config"
})
public class HotelManagementSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(HotelManagementSystemApplication.class, args);
    }
}