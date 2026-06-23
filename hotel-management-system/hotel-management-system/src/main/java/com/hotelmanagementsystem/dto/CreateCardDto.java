package com.hotelmanagementsystem.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class CreateCardDto {
    @NotBlank(message = "Card number is required")
    @Pattern(regexp = "\\d{12}", message = "Card number must be exactly 12 digits")
    private String cardNumber;
    
    @NotBlank(message = "Card holder name is required")
    @Size(min = 2, max = 100, message = "Card holder name must be between 2 and 100 characters")
    private String cardHolderName;
    
    @NotBlank(message = "CVV is required")
    @Pattern(regexp = "\\d{3}", message = "CVV must be exactly 3 digits")
    private String cvv;
    
    @NotBlank(message = "Expiry date is required")
    @Pattern(regexp = "(0[1-9]|1[0-2])/\\d{2}", message = "Expiry date must be in MM/YY format")
    private String expiryDate;
    
    @NotBlank(message = "Card type is required")
    @Pattern(regexp = "CREDIT|DEBIT", message = "Card type must be either CREDIT or DEBIT")
    private String cardType;
    
    private boolean isDefault = false;
}
