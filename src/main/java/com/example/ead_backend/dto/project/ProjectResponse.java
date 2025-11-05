package com.example.ead_backend.dto.project;

import com.example.ead_backend.model.enums.ProjectStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProjectResponse {
    private String projectId;
    private String customerName;
    private String customerId;
    private String vehicleMake;
    private String vehicleModel;
    private String vehicleLicensePlate;
    private String serviceDescription;
    private String employeeName;
    private String employeeId;
    private ProjectStatus status;
    private String adminNotes;
    private String employeeNotes;
    private Double estimatedCost;
    private Integer estimatedDurationDays;
    private LocalDateTime createdAt;
    private LocalDateTime assignedAt;
    private LocalDateTime completedAt;
}
