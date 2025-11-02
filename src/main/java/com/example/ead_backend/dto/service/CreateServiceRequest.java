package com.example.ead_backend.dto.service;

import com.example.ead_backend.model.enums.ServiceStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateServiceRequest {
    
    @NotBlank(message = "Service name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Category ID is required")
    private Long categoryId;
    
    @Positive(message = "Duration must be positive")
    private Double durationInHours;
    
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;
    
    private String currency;
    
    private ServiceStatus status;
}
