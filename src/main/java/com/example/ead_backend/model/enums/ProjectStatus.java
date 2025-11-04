package com.example.ead_backend.model.enums;

public enum ProjectStatus {
    PENDING,        // Created by customer, waiting for assignment
    ASSIGNED,       // Assigned to an employee by admin
    IN_PROGRESS,    // Project work in progress
    COMPLETED,      // Project completed
    CANCELLED,      // Project cancelled
    ON_HOLD         // Project temporarily on hold
}
