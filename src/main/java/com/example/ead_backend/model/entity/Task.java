package com.example.ead_backend.model.entity;

import com.example.ead_backend.model.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "Tasks")
@Data
public class Task {
    @Id
    private String taskId;
    
    private String appointmentId;
    
    private String taskName;
    
    private String description;
    
    private String assignedEmployeeId;
    
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    
    private LocalDateTime dueDate;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointmentId", insertable = false, updatable = false)
    private Appointment appointment;
}
