package com.example.ead_backend.controller.customer;

import com.example.ead_backend.dto.appointment.AppointmentResponse;
import com.example.ead_backend.dto.appointment.CreateAppointmentRequest;
import com.example.ead_backend.dto.appointment.TimeSlotResponse;
import com.example.ead_backend.service.appointment.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/customer/appointments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Allow frontend to access
public class CustomerAppointmentController {
    
    private final AppointmentService appointmentService;
    
    /**
     * Create a new appointment
     * POST /api/customer/appointments
     */
    @PostMapping
    public ResponseEntity<AppointmentResponse> createAppointment(
            @Valid @RequestBody CreateAppointmentRequest request) {
        
        AppointmentResponse response = appointmentService.createAppointment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Get available time slots for a specific date
     * GET /api/customer/appointments/available-slots?date=2025-11-05
     */
    @GetMapping("/available-slots")
    public ResponseEntity<List<TimeSlotResponse>> getAvailableTimeSlots(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        List<TimeSlotResponse> slots = appointmentService.getAvailableTimeSlots(date);
        return ResponseEntity.ok(slots);
    }
    
    /**
     * Get customer's own appointments
     * GET /api/customer/appointments/{customerId}
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<List<AppointmentResponse>> getMyAppointments(@PathVariable String customerId) {
        List<AppointmentResponse> appointments = appointmentService.getCustomerAppointments(customerId);
        return ResponseEntity.ok(appointments);
    }
    
    /**
     * Get appointment by ID
     * GET /api/customer/appointments/detail/{appointmentId}
     */
    @GetMapping("/detail/{appointmentId}")
    public ResponseEntity<AppointmentResponse> getAppointmentById(@PathVariable String appointmentId) {
        AppointmentResponse appointment = appointmentService.getAppointmentById(appointmentId);
        return ResponseEntity.ok(appointment);
    }
    
    /**
     * Validate booking constraints
     * POST /api/customer/appointments/validate
     */
    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateBooking(
            @Valid @RequestBody CreateAppointmentRequest request) {
        
        boolean isValid = appointmentService.validateBookingConstraints(request);
        return ResponseEntity.ok(isValid);
    }
}
