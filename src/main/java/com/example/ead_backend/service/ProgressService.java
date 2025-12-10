package com.example.ead_backend.service;

import com.example.ead_backend.dto.ProgressResponse;
import com.example.ead_backend.dto.ProgressUpdateRequest;

import java.util.List;

/**
 * Service interface for managing progress updates on appointments.
 */
public interface ProgressService {

    /**
     * Create or update progress for an appointment.
     *
     * @param appointmentId 
     * @param request       
     * @param updatedBy     
     * @return 
     */
    ProgressResponse createOrUpdateProgress(String appointmentId, ProgressUpdateRequest request, Long updatedBy);

    /**
     * Get all progress updates for an appointment.
     *
     * @param appointmentId 
     * @return l
     */
    List<ProgressResponse> getProgressForAppointment(String appointmentId);

    /**
     * Calculate the overall progress percentage for an appointment.
     * Uses ProgressCalculationService for computation.
     *
     * @param appointmentId 
     * @return 
     */
    int calculateProgressPercentage(String appointmentId);
}
