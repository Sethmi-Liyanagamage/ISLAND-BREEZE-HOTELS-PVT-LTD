package com.hotelmanagementsystem.controller;

import com.hotelmanagementsystem.model.User;
import com.hotelmanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/register")
    public ResponseEntity<User> registerGuest(@RequestBody User user) {
        // Role is now set from the frontend based on user selection
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("GUEST");
        }
        return ResponseEntity.ok(userService.createUser(user));
    }
    
    @GetMapping("/validate-staff-id/{staffId}")
    public ResponseEntity<Void> validateStaffId(@PathVariable String staffId) {
        // Predefined valid staff IDs (in production, this should be in a database)
        List<String> validStaffIds = List.of("STAFF001", "STAFF002", "STAFF003", "ADMIN001", "ADMIN002");
        
        if (validStaffIds.contains(staffId)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(403).build(); // Forbidden
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable String id) {
        try {
            return userService.getUserById(id)
                    .map(user -> ResponseEntity.ok((Object) user))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching user: " + e.getMessage());
        }
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/guests")
    public ResponseEntity<List<User>> getAllGuests() {
        return ResponseEntity.ok(userService.getAllGuests());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody User user) {
        try {
            // Basic validation
            if (!id.equals(user.getId())) {
                return ResponseEntity.badRequest().body("User ID mismatch");
            }
            User updatedUser = userService.updateUser(user);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}