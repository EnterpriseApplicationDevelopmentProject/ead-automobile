package com.example.ead_backend.dto.appointment;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TimeSlotResponse {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean available;
}
