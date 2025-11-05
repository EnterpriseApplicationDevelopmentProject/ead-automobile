package com.example.ead_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDTO {
    private String id;
    private Long customerId;
    private String customerName;
    private Long vehicleId;
    private String vehicleNumber;
    private String vehicleModel;
    private String vehicleType;
    private Long employeeId;
    private String employeeName;
    private String assignedEmployee;
    private String serviceDescription;
    private String taskName;
    private String description;
    private String status;
    private String adminNotes;
    private String employeeNotes;
    private Double estimatedCost;
    private Integer estimatedDurationDays;
    private LocalDateTime startDate;
    private String time;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime assignedAt;
    private LocalDateTime completedAt;
}
