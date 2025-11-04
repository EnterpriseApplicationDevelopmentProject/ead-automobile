package com.example.ead_backend.model.enums;

public enum AppointmentStatus {
    PENDING,        // Created by customer, waiting for assignment
    ASSIGNED,       // Assigned to an employee by admin
    CONFIRMED,      // Employee confirmed the appointment
    IN_PROGRESS,    // Appointment is in progress
    COMPLETED,      // Appointment completed
    CANCELLED,      // Appointment cancelled
    NO_SHOW         // Customer didn't show up
}
