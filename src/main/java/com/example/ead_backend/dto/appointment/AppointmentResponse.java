package com.example.ead_backend.dto.appointment;

import com.example.ead_backend.model.enums.AppointmentStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AppointmentResponse {
    private String appointmentId;
    private String customerName;
    private String customerId;
    private String vehicleMake;
    private String vehicleModel;
    private String vehicleLicensePlate;
    private String employeeName;
    private String employeeId;
    private LocalDateTime appointmentTime;
    private AppointmentStatus status;
    private List<String> tasks;
    private String customerNotes;
    private Integer estimatedDurationMinutes;
    private LocalDateTime createdAt;
}
