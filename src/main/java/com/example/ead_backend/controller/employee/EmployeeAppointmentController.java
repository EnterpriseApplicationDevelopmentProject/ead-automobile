package com.example.ead_backend.controller.employee;

import com.example.ead_backend.dto.appointment.AppointmentResponse;
import com.example.ead_backend.service.appointment.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee/appointments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Allow frontend to access
public class EmployeeAppointmentController {
    
    private final AppointmentService appointmentService;
    
    /**
     * Get employee's assigned appointments
     * GET /api/employee/appointments/{employeeId}
     */
    @GetMapping("/{employeeId}")
    public ResponseEntity<List<AppointmentResponse>> getMyAppointments(@PathVariable String employeeId) {
        List<AppointmentResponse> appointments = appointmentService.getEmployeeAppointments(employeeId);
        return ResponseEntity.ok(appointments);
    }
}
