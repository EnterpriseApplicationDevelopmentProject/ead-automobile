package com.example.ead_backend.dto;

import com.example.ead_backend.model.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {
    private String appointmentId;
    private String customerId;
    private String vehicleId;
    private String description;
    private LocalDateTime appointmentDateTime;
    private AppointmentStatus status;
    private String assignedEmployeeId;
    private String assignedEmployeeName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
