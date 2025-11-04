package com.example.ead_backend.repository;

import com.example.ead_backend.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    
    // Find all available employees
    List<Employee> findByIsAvailable(boolean isAvailable);
    
    // Find employees by specialization who are available
    List<Employee> findBySpecializationAndIsAvailable(String specialization, boolean isAvailable);
    
    // Get all available employees with their current workload
    @Query("SELECT e FROM Employee e WHERE e.isAvailable = true")
    List<Employee> findAllAvailableEmployees();
}
