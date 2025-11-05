package com.example.ead_backend.model.enums;

public enum ProjectStatus {
    PENDING,           // Customer submitted, waiting for admin review
    UNDER_REVIEW,      // Admin is reviewing the request
    ASSIGNED,          // Admin assigned to employee
    IN_PROGRESS,       // Employee started working
    COMPLETED,         // Project finished
    CANCELLED          // Project cancelled
}
