package com.example.ead_backend.dto.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResponse {
    private Long id;
    private String serviceName;
    private String description;
    private BigDecimal price;
    private Integer estimatedDuration;
    private Long categoryId;
    private String categoryName;
    private Boolean isActive;
    
    public Boolean isActive() {
        return isActive;
    }
}
