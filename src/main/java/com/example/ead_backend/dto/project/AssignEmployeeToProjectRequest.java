package com.example.ead_backend.dto.project;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AssignEmployeeToProjectRequest {
    @NotBlank(message = "Employee ID is required")
    private String employeeId;
    
    private String adminNotes;
    private Double estimatedCost;
    private Integer estimatedDurationDays;
}
