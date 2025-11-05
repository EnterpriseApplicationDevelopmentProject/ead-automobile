package com.example.ead_backend.mapper;

import com.example.ead_backend.dto.project.ProjectResponse;
import com.example.ead_backend.model.entity.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {
    
    public ProjectResponse toResponse(Project project) {
        ProjectResponse response = new ProjectResponse();
        response.setProjectId(project.getProjectId());
        
        // Get customer name from User entity
        if (project.getCustomer() != null && project.getCustomer().getUser() != null) {
            response.setCustomerName(project.getCustomer().getUser().getFirstName() + " " + 
                                    project.getCustomer().getUser().getLastName());
            response.setCustomerId(String.valueOf(project.getCustomer().getId()));
        }
        
        if (project.getVehicle() != null) {
            response.setVehicleMake(project.getVehicle().getMake());
            response.setVehicleModel(project.getVehicle().getModel());
            response.setVehicleLicensePlate(project.getVehicle().getLicensePlate());
        }
        
        response.setServiceDescription(project.getServiceDescription());
        
        // Get employee name from User entity
        if (project.getEmployee() != null && project.getEmployee().getUser() != null) {
            response.setEmployeeName(project.getEmployee().getUser().getFirstName() + " " + 
                                    project.getEmployee().getUser().getLastName());
            response.setEmployeeId(String.valueOf(project.getEmployee().getId()));
        }
        
        response.setStatus(project.getStatus());
        response.setAdminNotes(project.getAdminNotes());
        response.setEmployeeNotes(project.getEmployeeNotes());
        response.setEstimatedCost(project.getEstimatedCost());
        response.setEstimatedDurationDays(project.getEstimatedDurationDays());
        response.setCreatedAt(project.getCreatedAt());
        response.setAssignedAt(project.getAssignedAt());
        response.setCompletedAt(project.getCompletedAt());
        
        return response;
    }
}
