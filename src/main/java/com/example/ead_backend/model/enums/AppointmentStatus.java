package com.example.ead_backend.model.enums;

public enum AppointmentStatus {
    PENDING,           // Customer created, waiting for admin assignment
    CONFIRMED,         // Admin assigned employee
    IN_PROGRESS,       // Employee started work
    COMPLETED,         // Work finished
    CANCELLED,         // Appointment cancelled
    NO_SHOW            // Customer didn't show up
}
