package com.example.ead_backend.repository;

import com.example.ead_backend.model.entity.TimeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeLogRepository extends JpaRepository<TimeLog, String> {

    /**
     * Get total hours logged for an appointment
     * 
     * @param appointmentId the appointment ID (as String)
     * @return total hours logged, or null if no logs exist
     */
    @Query("SELECT SUM(tl.hoursLogged) FROM TimeLog tl WHERE tl.appointment.appointmentId = :appointmentId")
    Double getTotalHoursLogged(@Param("appointmentId") String appointmentId);

    /**
     * Find all time logs for an appointment
     * 
     * @param appointmentId the appointment ID (as String)
     * @return list of time logs
     */
    List<TimeLog> findByAppointment_AppointmentId(String appointmentId);
}
