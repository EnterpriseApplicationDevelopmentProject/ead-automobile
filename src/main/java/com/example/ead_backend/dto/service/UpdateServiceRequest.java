package com.example.ead_backend.dto.service;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * DTO for updating an existing service
 * All fields are optional - only provided fields will be updated
 * Author: Member 9 - Dilminda W.W.C.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateServiceRequest {
    
    @Positive(message = "Category ID must be positive")
    private Long categoryId;
    
    @Size(min = 3, max = 255, message = "Name must be between 3 and 255 characters")
    private String name;
    
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
    
    @DecimalMin(value = "0.1", message = "Estimated hours must be at least 0.1")
    @DecimalMax(value = "999.99", message = "Estimated hours cannot exceed 999.99")
    @Digits(integer = 3, fraction = 2, message = "Estimated hours must have at most 3 digits and 2 decimal places")
    private BigDecimal estimatedHours;
    
    @DecimalMin(value = "0.00", message = "Price must be positive")
    @DecimalMax(value = "9999999.99", message = "Price is too high")
    @Digits(integer = 7, fraction = 2, message = "Price must have at most 7 digits and 2 decimal places")
    private BigDecimal price;
    
    private Boolean isActive;
}