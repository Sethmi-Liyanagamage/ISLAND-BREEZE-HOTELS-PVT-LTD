package com.hotelmanagementsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index.html";
    }

    @GetMapping("/index")
    public String index() {
        return "index.html";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin-login.html";
    }

    @GetMapping("/admin-dashboard")
    public String adminDashboard() {
        return "admin-dashboard.html";
    }

    @GetMapping("/guest-dashboard")
    public String guestDashboard() {
        return "guest-dashboard.html";
    }
}
