package com.example.ead_backend.repository;

import com.example.ead_backend.model.entity.Project;
import com.example.ead_backend.model.enums.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {
    
    // Find all projects with PENDING status (not yet assigned)
    List<Project> findByStatus(ProjectStatus status);
    
    // Find all projects assigned to a specific employee
    List<Project> findByAssignedEmployeeId(String employeeId);
    
    // Find all pending projects (waiting for admin assignment)
    @Query("SELECT p FROM Project p WHERE p.status = 'PENDING' OR p.assignedEmployeeId IS NULL")
    List<Project> findAllPendingProjects();
}
