package com.example.ead_backend.service.project;

import com.example.ead_backend.dto.project.*;
import com.example.ead_backend.mapper.ProjectMapper;
import com.example.ead_backend.model.entity.Customer;
import com.example.ead_backend.model.entity.Employee;
import com.example.ead_backend.model.entity.Project;
import com.example.ead_backend.model.entity.Vehicle;
import com.example.ead_backend.model.enums.ProjectStatus;
import com.example.ead_backend.repository.CustomerRepository;
import com.example.ead_backend.repository.EmployeeRepository;
import com.example.ead_backend.repository.ProjectRepository;
import com.example.ead_backend.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectMapper projectMapper;

    /**
     * Customer creates a project request
     */
    @Transactional
    public ProjectResponse createProject(CreateProjectRequest request) {
        // Validate customer
        Customer customer = customerRepository.findById(Long.valueOf(request.getCustomerId()))
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Validate vehicle
        Vehicle vehicle = vehicleRepository.findById(Long.valueOf(request.getVehicleId()))
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        if (!vehicle.getCustomer().getId().equals(customer.getId())) {
            throw new RuntimeException("Vehicle does not belong to customer");
        }

        // Create project
        Project project = new Project();
        project.setCustomer(customer);
        project.setVehicle(vehicle);
        project.setServiceDescription(request.getServiceDescription());
        project.setStatus(ProjectStatus.PENDING);

        Project saved = projectRepository.save(project);

        return projectMapper.toResponse(saved);
    }

    /**
     * Admin assigns employee to project
     */
    @Transactional
    public ProjectResponse assignEmployeeToProject(String projectId, AssignEmployeeToProjectRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Employee employee = employeeRepository.findById(Long.valueOf(request.getEmployeeId()))
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        project.setEmployee(employee);
        project.setStatus(ProjectStatus.ASSIGNED);
        project.setAdminNotes(request.getAdminNotes());
        project.setEstimatedCost(request.getEstimatedCost());
        project.setEstimatedDurationDays(request.getEstimatedDurationDays());
        project.setAssignedAt(LocalDateTime.now());

        Project updated = projectRepository.save(project);

        return projectMapper.toResponse(updated);
    }

    /**
     * Get all pending projects (for admin review)
     */
    public List<ProjectResponse> getPendingProjects() {
        return projectRepository.findByStatusOrderByCreatedAtDesc(ProjectStatus.PENDING)
                .stream()
                .map(projectMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all projects (for admin)
     */
    public List<ProjectResponse> getAllProjects() {
        return projectRepository.findAll()
                .stream()
                .map(projectMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get customer's projects
     */
    public List<ProjectResponse> getCustomerProjects(String customerId) {
        return projectRepository.findByCustomer_CustomerId(customerId)
                .stream()
                .map(projectMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get employee's assigned projects
     */
    public List<ProjectResponse> getEmployeeProjects(String employeeId) {
        return projectRepository.findByEmployee_EmployeeId(employeeId)
                .stream()
                .map(projectMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get project by ID
     */
    public ProjectResponse getProjectById(String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        return projectMapper.toResponse(project);
    }

    /**
     * Employee updates project status
     */
    @Transactional
    public ProjectResponse updateProjectStatus(String projectId, ProjectStatus status,
            UpdateProjectStatusRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        project.setStatus(status);
        if (request.getEmployeeNotes() != null) {
            project.setEmployeeNotes(request.getEmployeeNotes());
        }

        if (status == ProjectStatus.COMPLETED) {
            project.setCompletedAt(LocalDateTime.now());
        }

        Project updated = projectRepository.save(project);
        return projectMapper.toResponse(updated);
    }
}
