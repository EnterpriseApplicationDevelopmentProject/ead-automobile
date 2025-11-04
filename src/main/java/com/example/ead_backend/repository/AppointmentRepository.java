package com.example.ead_backend.repository;

import com.example.ead_backend.model.entity.Appointment;
import com.example.ead_backend.model.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {
    
    // Find all appointments with PENDING status (not yet assigned)
    List<Appointment> findByStatus(AppointmentStatus status);
    
    // Find all appointments assigned to a specific employee
    List<Appointment> findByAssignedEmployeeId(String employeeId);
    
    // Find all pending appointments (waiting for admin assignment)
    @Query("SELECT a FROM Appointment a WHERE a.status = 'PENDING' OR a.assignedEmployeeId IS NULL")
    List<Appointment> findAllPendingAppointments();
}
