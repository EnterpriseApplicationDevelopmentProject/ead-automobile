package com.example.ead_backend.repository;

import com.example.ead_backend.model.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
    
    // Find all tasks for a specific appointment
    List<Task> findByAppointmentId(String appointmentId);
    
    // Find tasks by status
    List<Task> findByStatus(String status);
    
    // Find tasks assigned to a specific employee
    List<Task> findByAssignedEmployeeId(String employeeId);
}
