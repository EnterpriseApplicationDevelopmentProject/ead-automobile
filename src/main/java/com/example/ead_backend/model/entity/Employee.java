package com.example.ead_backend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employees")
@Data
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String employeeId;
    
    private String firstName;
    private String lastName;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    private String password;
    private String phone;
    private String specialization; // e.g., "Mechanic", "Electrician"
    
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Appointment> assignedAppointments;
    
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Project> assignedProjects;
    
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<TimeLog> timeLogs;
    
    private boolean isAvailable = true;
    private LocalDateTime createdAt = LocalDateTime.now();
}