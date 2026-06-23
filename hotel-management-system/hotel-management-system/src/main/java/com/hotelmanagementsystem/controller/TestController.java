package com.hotelmanagementsystem.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private final JdbcTemplate jdbcTemplate;

    public TestController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/tables")
    public List<Map<String, Object>> listTables() {
        return jdbcTemplate.queryForList("SHOW TABLES FROM hotel_db");
    }

    @GetMapping("/contact-messages")
    public List<Map<String, Object>> listContactMessages() {
        try {
            return jdbcTemplate.queryForList("SELECT * FROM hotel_db.contact_messages");
        } catch (Exception e) {
            throw new RuntimeException("Error querying contact_messages table: " + e.getMessage(), e);
        }
    }
}
