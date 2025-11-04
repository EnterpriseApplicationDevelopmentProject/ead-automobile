package com.example.ead_backend.dto;

import com.example.ead_backend.model.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private String taskId;
    private String appointmentId;
    private String taskName;
    private String description;
    private String assignedEmployeeId;
    private String assignedEmployeeName;
    private TaskStatus status;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
