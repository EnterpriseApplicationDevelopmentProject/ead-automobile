package com.example.ead_backend.controller.admin;

import com.example.ead_backend.dto.appointment.AppointmentResponse;
import com.example.ead_backend.dto.appointment.AssignEmployeeRequest;
import com.example.ead_backend.service.appointment.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/appointments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Allow frontend to access
public class AdminAppointmentController {
    
    private final AppointmentService appointmentService;
    
    /**
     * Get all pending appointments
     * GET /api/admin/appointments/pending
     */
    @GetMapping("/pending")
    public ResponseEntity<List<AppointmentResponse>> getPendingAppointments() {
        List<AppointmentResponse> appointments = appointmentService.getPendingAppointments();
        return ResponseEntity.ok(appointments);
    }
    
    /**
     * Get all appointments
     * GET /api/admin/appointments
     */
    @GetMapping
    public ResponseEntity<List<AppointmentResponse>> getAllAppointments() {
        List<AppointmentResponse> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }
    
    /**
     * Assign employee to appointment
     * PUT /api/admin/appointments/{appointmentId}/assign-employee
     */
    @PutMapping("/{appointmentId}/assign-employee")
    public ResponseEntity<AppointmentResponse> assignEmployee(
            @PathVariable String appointmentId,
            @Valid @RequestBody AssignEmployeeRequest request) {
        
        AppointmentResponse response = appointmentService.assignEmployee(appointmentId, request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get appointment by ID
     * GET /api/admin/appointments/{appointmentId}
     */
    @GetMapping("/{appointmentId}")
    public ResponseEntity<AppointmentResponse> getAppointmentById(@PathVariable String appointmentId) {
        AppointmentResponse appointment = appointmentService.getAppointmentById(appointmentId);
        return ResponseEntity.ok(appointment);
    }
}
