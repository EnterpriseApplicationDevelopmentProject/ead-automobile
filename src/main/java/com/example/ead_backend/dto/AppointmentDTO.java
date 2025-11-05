package com.example.ead_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDTO {
    private String id;
    private Long customerId;
    private String customerName;
    private Long vehicleId;
    private String vehicleNumber;
    private String vehicleModel;
    private Long employeeId;
    private String employeeName;
    private LocalDateTime appointmentTime;
    private String status;
    private List<String> tasks;
    private String serviceName;
    private String customerNotes;
    private Integer estimatedDurationMinutes;
    private LocalDateTime createdAt;
}
