package com.example.ead_backend.repository;

import com.example.ead_backend.model.entity.Appointment;
import com.example.ead_backend.model.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {
    
    // Find appointments by customer
    List<Appointment> findByCustomer_Id(Long customerId);
    
    // Find appointments by employee
    List<Appointment> findByEmployee_Id(Long employeeId);
    
    // Find appointments by status
    List<Appointment> findByStatus(AppointmentStatus status);
    
    // Find appointments by date range
    @Query("SELECT a FROM Appointment a WHERE a.appointmentTime BETWEEN :startDate AND :endDate")
    List<Appointment> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                      @Param("endDate") LocalDateTime endDate);
    
    // Check for conflicting appointments (same time slot)
    @Query("SELECT a FROM Appointment a WHERE a.appointmentTime = :appointmentTime " +
           "AND a.status NOT IN ('CANCELLED', 'COMPLETED')")
    List<Appointment> findConflictingAppointments(@Param("appointmentTime") LocalDateTime appointmentTime);
    
    // Find employee appointments at specific time
    @Query("SELECT a FROM Appointment a WHERE CAST(a.employee.id AS string) = :employeeId " +
           "AND a.appointmentTime = :appointmentTime " +
           "AND (a.status = com.example.ead_backend.model.enums.AppointmentStatus.CONFIRMED " +
           "OR a.status = com.example.ead_backend.model.enums.AppointmentStatus.IN_PROGRESS)")
    List<Appointment> findEmployeeAppointmentsAtTime(@Param("employeeId") String employeeId,
                                                      @Param("appointmentTime") LocalDateTime appointmentTime);
}
