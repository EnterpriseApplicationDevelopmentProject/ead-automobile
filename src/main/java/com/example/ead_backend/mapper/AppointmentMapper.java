package com.example.ead_backend.mapper;

import com.example.ead_backend.dto.appointment.AppointmentResponse;
import com.example.ead_backend.model.entity.Appointment;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    public AppointmentResponse toResponse(Appointment appointment) {
        AppointmentResponse response = new AppointmentResponse();
        response.setAppointmentId(appointment.getAppointmentId());

        // Get customer name from User entity
        if (appointment.getCustomer() != null && appointment.getCustomer().getUser() != null) {
            response.setCustomerName(appointment.getCustomer().getUser().getFirstName() + " " +
                    appointment.getCustomer().getUser().getLastName());
            response.setCustomerId(String.valueOf(appointment.getCustomer().getId()));
        }

        if (appointment.getVehicle() != null) {
            response.setVehicleMake(appointment.getVehicle().getMake());
            response.setVehicleModel(appointment.getVehicle().getModel());
            response.setVehicleLicensePlate(appointment.getVehicle().getLicensePlate());
        }

        // Get employee name from User entity
        if (appointment.getEmployee() != null && appointment.getEmployee().getUser() != null) {
            response.setEmployeeName(appointment.getEmployee().getUser().getFirstName() + " " +
                    appointment.getEmployee().getUser().getLastName());
            response.setEmployeeId(String.valueOf(appointment.getEmployee().getId()));
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
