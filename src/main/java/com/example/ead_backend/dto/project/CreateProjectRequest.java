package com.example.ead_backend.dto.project;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateProjectRequest {
    @NotBlank(message = "Customer ID is required")
    private String customerId;
    
    @NotBlank(message = "Vehicle ID is required")
    private String vehicleId;
    
    @NotBlank(message = "Service description is required")
    private String serviceDescription; // Text area content - what service customer needs
}
