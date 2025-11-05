package com.example.ead_backend.service;

import com.example.ead_backend.model.entity.Appointment;
import com.example.ead_backend.model.entity.ProgressUpdate;
import com.example.ead_backend.repository.ProgressUpdateRepository;
import com.example.ead_backend.repository.TimeLogRepository;
import com.example.ead_backend.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for calculating progress percentages based on recorded updates and
 * time logged.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProgressCalculationService {

    private final ProgressUpdateRepository progressUpdateRepository;
    private final TimeLogRepository timeLogRepository;
    private final AppointmentRepository appointmentRepository;

    /**
     * Calculate the average progress percentage for an appointment.
     * Uses the average of all recorded percentage entries.
     *
     * @param appointmentId the appointment ID
     * @return the calculated average percentage
     */
    public int calculateAverageProgress(Long appointmentId) {
        List<ProgressUpdate> updates = progressUpdateRepository.findByAppointmentIdOrderByCreatedAtAsc(appointmentId);

        if (updates.isEmpty()) {
            log.debug("No progress updates found for appointment {}", appointmentId);
            return 0;
        }

        int sum = updates.stream()
                .mapToInt(ProgressUpdate::getPercentage)
                .sum();

        int average = sum / updates.size();
        log.debug("Calculated average progress for appointment {}: {}%", appointmentId, average);

        return average;
    }

    /**
     * Get the latest progress percentage for an appointment.
     *
     * @param appointmentId the appointment ID
     * @return the latest percentage or 0 if no updates exist
     */
    public int getLatestProgress(Long appointmentId) {
        List<ProgressUpdate> updates = progressUpdateRepository.findByAppointmentIdOrderByCreatedAtAsc(appointmentId);

        if (updates.isEmpty()) {
            return 0;
        }

        return updates.get(updates.size() - 1).getPercentage();
    }

    /**
     * Calculate time-based progress for an appointment.
     * Compares actual time logged against estimated time.
     *
     * Formula: (Total Time Logged / Estimated Time) * 100
     *
     * @param appointmentId the appointment ID
     * @return time-based progress percentage (0-100+, can exceed 100 if overtime)
     */
    public int calculateTimeBasedProgress(Long appointmentId) {
        try {
            // 1. Get total time logged
            Double totalLoggedHours = timeLogRepository.getTotalHoursLogged(String.valueOf(appointmentId));

            if (totalLoggedHours == null || totalLoggedHours == 0) {
                log.debug("No time logged yet for appointment {}", appointmentId);
                return 0;
            }

            // 2. Get appointment estimated hours
            Appointment appointment = appointmentRepository.findById(String.valueOf(appointmentId))
                    .orElse(null);

            if (appointment == null) {
                log.warn("Appointment {} not found", appointmentId);
                return 0;
            }

            Integer estimatedDurationMinutes = appointment.getEstimatedDurationMinutes();

            if (estimatedDurationMinutes == null || estimatedDurationMinutes <= 0) {
                log.debug("No estimated duration set for appointment {}", appointmentId);
                return 0;
            }

            // Convert minutes to hours
            Double estimatedHours = estimatedDurationMinutes / 60.0;

            // 3. Calculate percentage: (logged / estimated) * 100
            int percentage = (int) Math.round((totalLoggedHours / estimatedHours) * 100);

            log.debug("Time-based progress for appointment {}: {}h / {}h = {}%",
                    appointmentId, totalLoggedHours, estimatedHours, percentage);

            // Allow percentage to exceed 100 to indicate overtime
            return percentage;

        } catch (Exception e) {
            log.error("Error calculating time-based progress for appointment {}", appointmentId, e);
            return 0;
        }
    }

    /**
     * Check if appointment is behind schedule based on time analysis.
     *
     * @param appointmentId         the appointment ID
     * @param currentManualProgress current manually entered progress %
     * @return true if time elapsed exceeds progress by significant margin
     */
    public boolean isAppointmentDelayed(Long appointmentId, int currentManualProgress) {
        try {
            // Calculate time-based progress (how much time has been used)
            int timeBasedProgress = calculateTimeBasedProgress(appointmentId);

            if (timeBasedProgress == 0) {
                // No time logged yet, can't determine delay
                return false;
            }

            // Calculate the gap: if time used exceeds work progress, it's delayed
            // Example: 80% of time used but only 50% work done = 30% gap = delayed
            int progressGap = timeBasedProgress - currentManualProgress;

            // Threshold: If gap exceeds 20%, consider it delayed
            boolean isDelayed = progressGap > 20;

            if (isDelayed) {
                log.warn("Appointment {} is DELAYED: {}% time used but only {}% work completed (gap: {}%)",
                        appointmentId, timeBasedProgress, currentManualProgress, progressGap);
            } else {
                log.debug("Appointment {} is on schedule: {}% time used, {}% work completed",
                        appointmentId, timeBasedProgress, currentManualProgress);
            }

            return isDelayed;

        } catch (Exception e) {
            log.error("Error checking delay status for appointment {}", appointmentId, e);
            return false;
        }
    }
}
