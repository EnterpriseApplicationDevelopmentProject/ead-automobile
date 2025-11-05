package com.example.ead_backend.dto.appointment;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AssignEmployeeRequest {
    @NotBlank(message = "Employee ID is required")
    private String employeeId;
}
