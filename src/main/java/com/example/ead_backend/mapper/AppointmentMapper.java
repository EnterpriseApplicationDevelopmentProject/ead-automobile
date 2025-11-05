package com.example.ead_backend.mapper;

import com.example.ead_backend.dto.appointment.AppointmentResponse;
import com.example.ead_backend.model.entity.Appointment;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {
    
    public AppointmentResponse toResponse(Appointment appointment) {
        AppointmentResponse response = new AppointmentResponse();
        response.setAppointmentId(appointment.getAppointmentId());
        response.setCustomerName(appointment.getCustomer().getFirstName() + " " + 
                                appointment.getCustomer().getLastName());
        response.setCustomerId(appointment.getCustomer().getCustomerId());
        
        if (appointment.getVehicle() != null) {
            response.setVehicleMake(appointment.getVehicle().getMake());
            response.setVehicleModel(appointment.getVehicle().getModel());
            response.setVehicleLicensePlate(appointment.getVehicle().getLicensePlate());
        }
        
        if (appointment.getEmployee() != null) {
            response.setEmployeeName(appointment.getEmployee().getFirstName() + " " + 
                                    appointment.getEmployee().getLastName());
            response.setEmployeeId(appointment.getEmployee().getEmployeeId());
        }
        
        response.setAppointmentTime(appointment.getAppointmentTime());
        response.setStatus(appointment.getStatus());
        response.setTasks(appointment.getTasks());
        response.setCustomerNotes(appointment.getCustomerNotes());
        response.setEstimatedDurationMinutes(appointment.getEstimatedDurationMinutes());
        response.setCreatedAt(appointment.getCreatedAt());
        
        return response;
    }
}
