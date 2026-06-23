// File: src/main/java/com/islandbreeze/hotelmgmtsystem/dto/AdminRegistrationDto.java

package com.hotelmanagementsystem.dto;

import lombok.Data;

@Data
public class AdminRegistrationDto {
    private String fullName;
    private String email;
    private String username;
    private String password;
    private String role;
}