// File: src/main/java/com/islandbreeze/hotelmgmtsystem/model/Admin.java

package com.hotelmanagementsystem.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "admins")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String fullName;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String username;
    private String password; // hash in production
    private String role;
    private String createdAt;
}