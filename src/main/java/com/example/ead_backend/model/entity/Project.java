package com.example.ead_backend.model.entity;

import com.example.ead_backend.model.enums.ProjectStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "projects")
@Data
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String projectId;
    
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;
    
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = true)
    private Employee employee;
    
    @Column(length = 2000, nullable = false)
    private String serviceDescription; // Customer describes what service they need
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status = ProjectStatus.PENDING;
    
    @Column(length = 1000)
    private String adminNotes; // Admin can add notes
    
    @Column(length = 1000)
    private String employeeNotes; // Employee can add progress notes
    
    private Double estimatedCost;
    
    private Integer estimatedDurationDays;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    private LocalDateTime updatedAt;
    
    private LocalDateTime assignedAt; // When admin assigned employee
    
    private LocalDateTime completedAt; // When project completed
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
