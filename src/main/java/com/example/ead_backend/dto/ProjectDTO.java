package com.example.ead_backend.dto;

import com.example.ead_backend.model.enums.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    private String projectId;
    private String customerId;
    private String vehicleId;
    private String projectName;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime expectedEndDate;
    private ProjectStatus status;
    private String assignedEmployeeId;
    private String assignedEmployeeName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
