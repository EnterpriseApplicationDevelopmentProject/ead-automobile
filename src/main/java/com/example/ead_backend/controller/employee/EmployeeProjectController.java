package com.example.ead_backend.controller.employee;

import com.example.ead_backend.dto.project.ProjectResponse;
import com.example.ead_backend.dto.project.UpdateProjectStatusRequest;
import com.example.ead_backend.model.enums.ProjectStatus;
import com.example.ead_backend.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee/projects")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Allow frontend to access
public class EmployeeProjectController {
    
    private final ProjectService projectService;
    
    /**
     * Get employee's assigned projects
     * GET /api/employee/projects/{employeeId}
     */
    @GetMapping("/{employeeId}")
    public ResponseEntity<List<ProjectResponse>> getMyProjects(@PathVariable String employeeId) {
        List<ProjectResponse> projects = projectService.getEmployeeProjects(employeeId);
        return ResponseEntity.ok(projects);
    }
    
    /**
     * Update project status
     * PUT /api/employee/projects/{projectId}/status
     */
    @PutMapping("/{projectId}/status")
    public ResponseEntity<ProjectResponse> updateProjectStatus(
            @PathVariable String projectId,
            @RequestParam ProjectStatus status,
            @RequestBody(required = false) UpdateProjectStatusRequest request) {
        
        if (request == null) {
            request = new UpdateProjectStatusRequest();
        }
        
        ProjectResponse response = projectService.updateProjectStatus(projectId, status, request);
        return ResponseEntity.ok(response);
    }
}
