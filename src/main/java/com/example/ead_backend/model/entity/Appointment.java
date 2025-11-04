package com.example.ead_backend.model.entity;

import com.example.ead_backend.model.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "Appointments")
@Data
public class Appointment {
    @Id
    private String appointmentId;
    
    private String customerId;
    
    private String vehicleId;
    
    private String description;
    
    private LocalDateTime appointmentDateTime;
    
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status = AppointmentStatus.PENDING;
    
    private String assignedEmployeeId;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignedEmployeeId", insertable = false, updatable = false)
    private Employee assignedEmployee;
}
