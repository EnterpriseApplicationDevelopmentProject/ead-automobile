package com.example.ead_backend.dto.service;

import com.example.ead_backend.model.enums.ServiceStatus;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateServiceRequest {
    
    private String name;
    
    private String description;
    
    private Long categoryId;
    
    @Positive(message = "Duration must be positive")
    private Double durationInHours;
    
    @Positive(message = "Price must be positive")
    private Double price;
    
    private String currency;
    
    private ServiceStatus status;
}
