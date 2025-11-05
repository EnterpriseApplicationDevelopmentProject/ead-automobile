package com.example.ead_backend.controller.admin;

import com.example.ead_backend.dto.project.AssignEmployeeToProjectRequest;
import com.example.ead_backend.dto.project.ProjectResponse;
import com.example.ead_backend.service.project.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/projects")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Allow frontend to access
public class AdminProjectController {
    
    private final ProjectService projectService;
    
    /**
     * Get all pending projects
     * GET /api/admin/projects/pending
     */
    @GetMapping("/pending")
    public ResponseEntity<List<ProjectResponse>> getPendingProjects() {
        List<ProjectResponse> projects = projectService.getPendingProjects();
        return ResponseEntity.ok(projects);
    }
    
    /**
     * Get all projects
     * GET /api/admin/projects
     */
    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {
        List<ProjectResponse> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }
    
    /**
     * Assign employee to project
     * PUT /api/admin/projects/{projectId}/assign-employee
     */
    @PutMapping("/{projectId}/assign-employee")
    public ResponseEntity<ProjectResponse> assignEmployee(
            @PathVariable String projectId,
            @Valid @RequestBody AssignEmployeeToProjectRequest request) {
        
        ProjectResponse response = projectService.assignEmployeeToProject(projectId, request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get project by ID
     * GET /api/admin/projects/{projectId}
     */
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable String projectId) {
        ProjectResponse project = projectService.getProjectById(projectId);
        return ResponseEntity.ok(project);
    }
}
