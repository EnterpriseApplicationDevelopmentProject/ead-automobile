package com.example.ead_backend.repository;

import com.example.ead_backend.model.entity.Project;
import com.example.ead_backend.model.enums.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {
    
    // Find projects by customer
    List<Project> findByCustomer_CustomerId(String customerId);
    
    // Find projects by employee
    List<Project> findByEmployee_EmployeeId(String employeeId);
    
    // Find projects by status
    List<Project> findByStatus(ProjectStatus status);
    
    // Find all pending projects (for admin to review)
    List<Project> findByStatusOrderByCreatedAtDesc(ProjectStatus status);
}
