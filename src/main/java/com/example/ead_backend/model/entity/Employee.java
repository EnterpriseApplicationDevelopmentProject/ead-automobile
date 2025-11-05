package com.example.ead_backend.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.example.ead_backend.model.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employees")
@Data
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phone;
    private String specialization; // e.g., "Mechanic", "Electrician"

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // ADMIN or EMPLOYEE

    @Column(nullable = false)
    private LocalDate joinedDate;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Appointment> assignedAppointments;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Project> assignedProjects;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<TimeLog> timeLogs;

    private boolean isAvailable = true;
    private LocalDateTime createdAt = LocalDateTime.now();

    public Employee(User user, Role role, LocalDate joinedDate) {
        this.user = user;
        this.role = role;
        this.joinedDate = joinedDate;
    }
}
