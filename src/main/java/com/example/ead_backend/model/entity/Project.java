package com.example.ead_backend.model.entity;

import com.example.ead_backend.model.enums.ProjectStatus;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "Projects")
@Data
public class Project {
    @Id
    private String projectId;
    
    private String customerId;
    
    private String vehicleId;
    
    private String projectName;
    
    private String description;
    
    private LocalDateTime startDate;
    
    private LocalDateTime expectedEndDate;
    
    @Enumerated(EnumType.STRING)
    private ProjectStatus status = ProjectStatus.PENDING;
    
    private String assignedEmployeeId;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignedEmployeeId", insertable = false, updatable = false)
    private Employee assignedEmployee;
}
