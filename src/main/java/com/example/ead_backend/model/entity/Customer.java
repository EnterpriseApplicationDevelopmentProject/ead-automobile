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
@Table(name = "customers")
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String customerId;
    
    private String firstName;
    private String lastName;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    private String password;
    private String phone;
    private String address;
    
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Vehicle> vehicles;
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Appointment> appointments;
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Project> projects;
    
    private LocalDateTime createdAt = LocalDateTime.now();
}
