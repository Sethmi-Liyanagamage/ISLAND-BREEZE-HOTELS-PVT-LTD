package com.hotelmanagementsystem.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class UpdateCardDto {
    @Size(min = 2, max = 100, message = "Card holder name must be between 2 and 100 characters")
    private String cardHolderName;
    
    @Pattern(regexp = "\\d{3}", message = "CVV must be exactly 3 digits")
    private String cvv;
    
    @Pattern(regexp = "(0[1-9]|1[0-2])/\\d{2}", message = "Expiry date must be in MM/YY format")
    private String expiryDate;
    
    @Pattern(regexp = "CREDIT|DEBIT", message = "Card type must be either CREDIT or DEBIT")
    private String cardType;
    
    private Boolean isDefault;
}
