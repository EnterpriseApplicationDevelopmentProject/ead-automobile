package com.example.ead_backend.controller;

import com.example.ead_backend.dto.AppointmentDTO;
import com.example.ead_backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AppointmentController {

    private final DashboardService dashboardService;

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AppointmentDTO>> getCustomerAppointments(@PathVariable Long customerId) {
        // This returns all appointments for the customer
        List<AppointmentDTO> appointments = dashboardService.getAllAppointments(customerId);
        return ResponseEntity.ok(appointments);
    }

    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<String> deleteAppointment(@PathVariable String appointmentId) {
        // TODO: Implement delete appointment logic
        return ResponseEntity.ok("Appointment deleted successfully");
    }
}
