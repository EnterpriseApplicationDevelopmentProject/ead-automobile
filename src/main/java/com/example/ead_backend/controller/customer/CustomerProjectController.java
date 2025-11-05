package com.example.ead_backend.controller.customer;

import com.example.ead_backend.dto.project.CreateProjectRequest;
import com.example.ead_backend.dto.project.ProjectResponse;
import com.example.ead_backend.service.project.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer/projects")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Allow frontend to access
public class CustomerProjectController {
    
    private final ProjectService projectService;
    
    /**
     * Create a new project request
     * POST /api/customer/projects
     */
    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
            @Valid @RequestBody CreateProjectRequest request) {
        
        ProjectResponse response = projectService.createProject(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Get customer's own projects
     * GET /api/customer/projects/{customerId}
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<List<ProjectResponse>> getMyProjects(@PathVariable String customerId) {
        List<ProjectResponse> projects = projectService.getCustomerProjects(customerId);
        return ResponseEntity.ok(projects);
    }
    
    /**
     * Get project by ID
     * GET /api/customer/projects/detail/{projectId}
     */
    @GetMapping("/detail/{projectId}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable String projectId) {
        ProjectResponse project = projectService.getProjectById(projectId);
        return ResponseEntity.ok(project);
    }
}
