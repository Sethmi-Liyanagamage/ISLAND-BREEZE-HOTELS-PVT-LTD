// File: src/main/java/com/islandbreeze/hotelmgmtsystem/dto/AdminLoginDto.java

package com.hotelmanagementsystem.dto;

import lombok.Data;

@Data
public class AdminLoginDto {
    private String username;
    private String password;
    private boolean rememberMe;
}