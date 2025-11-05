package com.example.ead_backend.controller;

import com.example.ead_backend.dto.ProjectDTO;
import com.example.ead_backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProjectController {

    private final DashboardService dashboardService;

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<ProjectDTO>> getCustomerProjects(@PathVariable Long customerId) {
        // This returns all projects for the customer
        List<ProjectDTO> projects = dashboardService.getAllProjects(customerId);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/customer/{customerId}/completed")
    public ResponseEntity<List<ProjectDTO>> getCompletedProjects(@PathVariable Long customerId) {
        List<ProjectDTO> projects = dashboardService.getCompletedProjects(customerId);
        return ResponseEntity.ok(projects);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<String> deleteProject(@PathVariable String projectId) {
        // TODO: Implement delete project logic
        return ResponseEntity.ok("Project deleted successfully");
    }
}
