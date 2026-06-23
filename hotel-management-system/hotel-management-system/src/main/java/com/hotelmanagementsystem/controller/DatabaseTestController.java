package com.hotelmanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hotelmanagementsystem.repository.AdminRepository;

@RestController
public class DatabaseTestController {
    @Autowired
    private AdminRepository adminRepository;

    @GetMapping("/db-test")
    public String testDatabase() {
        long count = adminRepository.count();
        return "Admin table row count: " + count;
    }
}
