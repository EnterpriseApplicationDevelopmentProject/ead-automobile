package com.example.ead_backend.model.entity;

import com.example.ead_backend.model.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "appointments")
@Data
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String appointmentId;
    
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;  // Changed to Customer
    
    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;
    
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = true)
    private Employee employee;  // Already using Employee
    
    @Column(nullable = false)
    private LocalDateTime appointmentTime;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status = AppointmentStatus.PENDING;
    
    @ElementCollection
    @CollectionTable(name = "appointment_tasks", joinColumns = @JoinColumn(name = "appointment_id"))
    @Column(name = "task")
    private List<String> tasks;
    
    @Column(length = 1000)
    private String customerNotes;
    
    private Integer estimatedDurationMinutes;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    private LocalDateTime updatedAt;
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
