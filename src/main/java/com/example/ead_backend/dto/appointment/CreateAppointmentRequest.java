package com.example.ead_backend.dto.appointment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateAppointmentRequest {
    @NotBlank(message = "Customer ID is required")
    private String customerId;
    
    @NotBlank(message = "Vehicle ID is required")
    private String vehicleId;
    
    @NotNull(message = "Appointment time is required")
    private LocalDateTime appointmentTime;
    
    @NotEmpty(message = "At least one task must be selected")
    private List<String> tasks;
    
    private String customerNotes;
}
