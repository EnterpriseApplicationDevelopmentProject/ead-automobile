package com.example.ead_backend.service.appointment;

import com.example.ead_backend.dto.appointment.*;
import com.example.ead_backend.mapper.AppointmentMapper;
import com.example.ead_backend.model.entity.Appointment;
import com.example.ead_backend.model.entity.Customer;
import com.example.ead_backend.model.entity.Employee;
import com.example.ead_backend.model.entity.Vehicle;
import com.example.ead_backend.model.enums.AppointmentStatus;
import com.example.ead_backend.repository.AppointmentRepository;
import com.example.ead_backend.repository.CustomerRepository;
import com.example.ead_backend.repository.EmployeeRepository;
import com.example.ead_backend.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    
    private final AppointmentRepository appointmentRepository;
    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final AppointmentMapper appointmentMapper;
    
    // Business hours configuration
    private static final LocalTime OPENING_TIME = LocalTime.of(8, 0);
    private static final LocalTime CLOSING_TIME = LocalTime.of(18, 0);
    private static final int SLOT_DURATION_MINUTES = 60;
    
    /**
     * Customer creates an appointment
     */
    @Transactional
    public AppointmentResponse createAppointment(CreateAppointmentRequest request) {
        // Validate vehicle belongs to customer
        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
            .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        
        Customer customer = customerRepository.findById(request.getCustomerId())
            .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        if (!vehicle.getOwner().getCustomerId().equals(customer.getCustomerId())) {
            throw new RuntimeException("Vehicle does not belong to customer");
        }
        
        // Check for conflicts
        List<Appointment> conflicts = appointmentRepository.findConflictingAppointments(request.getAppointmentTime());
        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Time slot is already booked");
        }
        
        // Calculate estimated duration based on tasks
        int estimatedDuration = calculateDuration(request.getTasks());
        
        // Create appointment
        Appointment appointment = new Appointment();
        appointment.setCustomer(customer);
        appointment.setVehicle(vehicle);
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setTasks(request.getTasks());
        appointment.setCustomerNotes(request.getCustomerNotes());
        appointment.setEstimatedDurationMinutes(estimatedDuration);
        appointment.setStatus(AppointmentStatus.PENDING);
        
        Appointment saved = appointmentRepository.save(appointment);
        
        return appointmentMapper.toResponse(saved);
    }
    
    /**
     * Get available time slots for a specific date
     */
    public List<TimeSlotResponse> getAvailableTimeSlots(LocalDate date) {
        List<TimeSlotResponse> timeSlots = new ArrayList<>();
        
        LocalDateTime startOfDay = date.atTime(OPENING_TIME);
        LocalDateTime endOfDay = date.atTime(CLOSING_TIME);
        
        // Get all appointments for the day
        List<Appointment> dayAppointments = appointmentRepository.findByDateRange(
            startOfDay, endOfDay.plusDays(1)
        );
        
        // Generate time slots
        LocalDateTime currentSlot = startOfDay;
        while (currentSlot.isBefore(endOfDay)) {
            LocalDateTime slotEnd = currentSlot.plusMinutes(SLOT_DURATION_MINUTES);
            
            // Check if slot is available
            final LocalDateTime finalCurrentSlot = currentSlot;
            boolean isBooked = dayAppointments.stream()
                .anyMatch(apt -> apt.getAppointmentTime().equals(finalCurrentSlot) &&
                                apt.getStatus() != AppointmentStatus.CANCELLED);
            
            timeSlots.add(new TimeSlotResponse(currentSlot, slotEnd, !isBooked));
            currentSlot = slotEnd;
        }
        
        return timeSlots;
    }
    
    /**
     * Admin assigns employee to appointment
     */
    @Transactional
    public AppointmentResponse assignEmployee(String appointmentId, AssignEmployeeRequest request) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new RuntimeException("Appointment not found"));
        
        Employee employee = employeeRepository.findById(request.getEmployeeId())
            .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        // Check if employee is available at that time
        List<Appointment> employeeConflicts = appointmentRepository
            .findEmployeeAppointmentsAtTime(employee.getEmployeeId(), appointment.getAppointmentTime());
        
        if (!employeeConflicts.isEmpty()) {
            throw new RuntimeException("Employee is not available at this time");
        }
        
        appointment.setEmployee(employee);
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        
        Appointment updated = appointmentRepository.save(appointment);
        
        return appointmentMapper.toResponse(updated);
    }
    
    /**
     * Get all pending appointments (for admin)
     */
    public List<AppointmentResponse> getPendingAppointments() {
        return appointmentRepository.findByStatus(AppointmentStatus.PENDING)
            .stream()
            .map(appointmentMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Get all appointments (for admin)
     */
    public List<AppointmentResponse> getAllAppointments() {
        return appointmentRepository.findAll()
            .stream()
            .map(appointmentMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Get customer's appointments
     */
    public List<AppointmentResponse> getCustomerAppointments(String customerId) {
        return appointmentRepository.findByCustomer_CustomerId(customerId)
            .stream()
            .map(appointmentMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Get employee's appointments
     */
    public List<AppointmentResponse> getEmployeeAppointments(String employeeId) {
        return appointmentRepository.findByEmployee_EmployeeId(employeeId)
            .stream()
            .map(appointmentMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Get appointment by ID
     */
    public AppointmentResponse getAppointmentById(String appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new RuntimeException("Appointment not found"));
        return appointmentMapper.toResponse(appointment);
    }
    
    /**
     * Calculate estimated duration based on tasks
     */
    private int calculateDuration(List<String> tasks) {
        // Simple calculation: 30 minutes per task
        // You can make this more sophisticated based on task types
        return tasks.size() * 30;
    }
    
    /**
     * Validate booking constraints
     */
    public boolean validateBookingConstraints(CreateAppointmentRequest request) {
        // Check if vehicle exists and belongs to customer
        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
            .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        
        if (!vehicle.getOwner().getCustomerId().equals(request.getCustomerId())) {
            return false;
        }
        
        // Check for time conflicts
        List<Appointment> conflicts = appointmentRepository
            .findConflictingAppointments(request.getAppointmentTime());
        
        return conflicts.isEmpty();
    }
}
