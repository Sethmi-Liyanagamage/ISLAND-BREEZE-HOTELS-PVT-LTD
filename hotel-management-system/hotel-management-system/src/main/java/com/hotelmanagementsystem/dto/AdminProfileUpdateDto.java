// File: src/main/java/com/islandbreeze/hotelmgmtsystem/dto/AdminProfileUpdateDto.java

package com.hotelmanagementsystem.dto;

import lombok.Data;

@Data
public class AdminProfileUpdateDto {
    private String id;
    private String fullName;
    private String email;
    private String password; // Optional: only for changing the password
}