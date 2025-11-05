package com.example.ead_backend.controller;

import com.example.ead_backend.dto.AppointmentDTO;
import com.example.ead_backend.dto.CustomerDTO;
import com.example.ead_backend.dto.DashboardStatsDTO;
import com.example.ead_backend.dto.ProjectDTO;
import com.example.ead_backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats/{customerId}")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats(@PathVariable Long customerId) {
        DashboardStatsDTO stats = dashboardService.getDashboardStats(customerId);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/profile/{customerId}")
    public ResponseEntity<CustomerDTO> getCustomerProfile(@PathVariable Long customerId) {
        CustomerDTO customer = dashboardService.getCustomerProfile(customerId);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/appointments/upcoming/{customerId}")
    public ResponseEntity<List<AppointmentDTO>> getUpcomingAppointments(@PathVariable Long customerId) {
        List<AppointmentDTO> appointments = dashboardService.getUpcomingAppointments(customerId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/projects/ongoing/{customerId}")
    public ResponseEntity<List<ProjectDTO>> getOngoingProjects(@PathVariable Long customerId) {
        List<ProjectDTO> projects = dashboardService.getOngoingProjects(customerId);
        return ResponseEntity.ok(projects);
    }
}
