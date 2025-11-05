package com.example.ead_backend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "time_logs")
@Data
public class TimeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String timeLogId;
    
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
    
    @ManyToOne
    @JoinColumn(name = "appointment_id", nullable = true)
    private Appointment appointment;
    
    @Column(nullable = false)
    private LocalDateTime startTime;
    
    private LocalDateTime endTime;
    
    private Integer durationMinutes;
    
    @Column(length = 500)
    private String notes;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    private LocalDateTime updatedAt;
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
