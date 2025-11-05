package com.example.ead_backend.model.enums;

/**
 * Enum representing different types of notifications in the system.
 */
public enum NotificationType {
    /**
     * Notification for progress updates on appointments
     */
    PROGRESS_UPDATE,

    /**
     * Notification for status changes in appointments or tasks
     */
    STATUS_CHANGE,

    /**
     * Notification for service completion (100% progress)
     */
    COMPLETION,

    /**
     * Alert notification for delays or time overruns
     */
    DELAY_ALERT,

    /**
     * General notifications
     */
    GENERAL
}
