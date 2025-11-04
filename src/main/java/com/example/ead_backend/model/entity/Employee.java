package com.example.ead_backend.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Employees")
@Data
public class Employee {
    @Id
    private String employeeId;
    
    private String name;
    
    private String email;
    
    private String phone;
    
    private String specialization;
    
    private boolean isAvailable = true;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "assignedEmployee", fetch = FetchType.LAZY)
    private List<Appointment> appointments;
    
    @OneToMany(mappedBy = "assignedEmployee", fetch = FetchType.LAZY)
    private List<Project> projects;
}
