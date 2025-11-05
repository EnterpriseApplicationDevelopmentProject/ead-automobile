package com.example.ead_backend.service.impl;

import com.example.ead_backend.dto.ProgressResponse;
import com.example.ead_backend.dto.ProgressUpdateRequest;
import com.example.ead_backend.mapper.ProgressMapper;
import com.example.ead_backend.model.entity.Notification;
import com.example.ead_backend.model.entity.ProgressUpdate;
import com.example.ead_backend.model.enums.NotificationType;
import com.example.ead_backend.repository.NotificationRepository;
import com.example.ead_backend.repository.ProgressUpdateRepository;
import com.example.ead_backend.service.EmailService;
import com.example.ead_backend.service.ProgressCalculationService;
import com.example.ead_backend.service.ProgressService;
import com.example.ead_backend.service.WebSocketNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of ProgressService for managing progress updates.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProgressServiceImpl implements ProgressService {

    private final ProgressUpdateRepository progressUpdateRepository;
    private final NotificationRepository notificationRepository;
    private final ProgressMapper progressMapper;
    private final ProgressCalculationService progressCalculationService;
    private final WebSocketNotificationService webSocketNotificationService;
    private final EmailService emailService;

    @Override
    @Transactional
    public ProgressResponse createOrUpdateProgress(Long appointmentId, ProgressUpdateRequest request, Long updatedBy) {
        log.info("Creating/updating progress for appointment {} by user {}", appointmentId, updatedBy);

        // Convert DTO to entity
        ProgressUpdate progressUpdate = progressMapper.toEntity(request);
        progressUpdate.setAppointmentId(appointmentId);
        progressUpdate.setUpdatedBy(updatedBy);

        // Save progress update
        ProgressUpdate saved = progressUpdateRepository.save(progressUpdate);
        log.debug("Progress update saved with ID: {}", saved.getId());

        // Calculate overall progress percentage (time-based calculation)
        int overallProgress = progressCalculationService.getLatestProgress(appointmentId);
        log.debug("Overall progress for appointment {}: {}%", appointmentId, overallProgress);

        // Create base notification
        String notificationMessage = String.format(
                "Progress updated for appointment #%d: %s (%d%%)",
                appointmentId, request.getStage(), request.getPercentage());

        NotificationType notificationType = NotificationType.PROGRESS_UPDATE;

        // Check for completion (100%) - trigger completion notification
        if (request.getPercentage() >= 100) {
            notificationMessage = String.format(
                    "Service completed for appointment #%d! Final stage: %s",
                    appointmentId, request.getStage());
            notificationType = NotificationType.COMPLETION;
            log.info("Appointment {} marked as COMPLETED (100%)", appointmentId);
        }

        // Check for delays (time overrun)
        // In real scenario, fetch estimated time from appointment and compare with
        // actual time logged
        // For now, we'll check if progress is less than expected at this time
        // Example: If 80% time passed but only 60% progress, it's delayed
        boolean isDelayed = checkForDelays(appointmentId, request.getPercentage());
        if (isDelayed && request.getPercentage() < 100) {
            notificationMessage = String.format(
                    "DELAY ALERT: Appointment #%d is behind schedule. Current progress: %d%%",
                    appointmentId, request.getPercentage());
            notificationType = NotificationType.DELAY_ALERT;
            log.warn("Appointment {} is DELAYED - progress behind schedule", appointmentId);
        }

        Notification notification = Notification.builder()
                .userId(updatedBy) // In real scenario, should notify customer
                .type(notificationType)
                .message(notificationMessage)
                .isRead(false)
                .build();

        notificationRepository.save(notification);
        log.debug("Notification created: type={}, message={}", notificationType, notificationMessage);

        // Convert to response
        ProgressResponse response = progressMapper.toResponse(saved);

        // Broadcast via WebSocket
        try {
            webSocketNotificationService.broadcastProgressUpdate(appointmentId, response);
        } catch (Exception e) {
            log.error("Failed to broadcast progress update via WebSocket: {}", e.getMessage());
        }

        // Send email notification (async in real scenario)
        try {
            emailService.sendProgressUpdateNotification(
                    "customer@example.com", // Should fetch from appointment/customer
                    appointmentId,
                    request.getStage(),
                    request.getPercentage(),
                    request.getRemarks());
        } catch (Exception e) {
            log.error("Failed to send email notification: {}", e.getMessage());
        }

        log.info("Progress update completed successfully for appointment {}", appointmentId);
        return response;
    }

    /**
     * Check if appointment is delayed based on time logged vs estimated time.
     * This is a simplified check - in production, fetch actual time data from
     * TimeLog.
     *
     * @param appointmentId   the appointment ID
     * @param currentProgress current progress percentage
     * @return true if delayed
     */
    private boolean checkForDelays(Long appointmentId, int currentProgress) {
        // TODO: In production, implement actual time comparison:
        // 1. Fetch appointment estimated time (hours)
        // 2. Sum all time logged from TimeLog table for this appointment
        // 3. Calculate time progress = (logged hours / estimated hours) * 100
        // 4. If time progress > current progress by threshold (e.g., 20%), it's delayed

        // Placeholder logic: assume delayed if progress < 50% (for demo)
        // Replace with actual time-based calculation when TimeLog data is available
        return currentProgress < 50;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgressResponse> getProgressForAppointment(Long appointmentId) {
        log.debug("Fetching progress history for appointment {}", appointmentId);

        List<ProgressUpdate> updates = progressUpdateRepository.findByAppointmentIdOrderByCreatedAtAsc(appointmentId);

        return updates.stream()
                .map(progressMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public int calculateProgressPercentage(Long appointmentId) {
        log.debug("Calculating progress percentage for appointment {}", appointmentId);
        return progressCalculationService.getLatestProgress(appointmentId);
    }
}
