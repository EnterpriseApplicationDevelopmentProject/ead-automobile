package com.example.ead_backend.mapper;

import com.example.ead_backend.dto.project.ProjectResponse;
import com.example.ead_backend.model.entity.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {
    
    public ProjectResponse toResponse(Project project) {
        ProjectResponse response = new ProjectResponse();
        response.setProjectId(project.getProjectId());
        response.setCustomerName(project.getCustomer().getFirstName() + " " + 
                                project.getCustomer().getLastName());
        response.setCustomerId(project.getCustomer().getCustomerId());
        
        if (project.getVehicle() != null) {
            response.setVehicleMake(project.getVehicle().getMake());
            response.setVehicleModel(project.getVehicle().getModel());
            response.setVehicleLicensePlate(project.getVehicle().getLicensePlate());
        }
        
        response.setServiceDescription(project.getServiceDescription());
        
        if (project.getEmployee() != null) {
            response.setEmployeeName(project.getEmployee().getFirstName() + " " + 
                                    project.getEmployee().getLastName());
            response.setEmployeeId(project.getEmployee().getEmployeeId());
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
