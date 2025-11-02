package com.example.ead_backend.mapper;

import com.example.ead_backend.dto.service.CreateServiceRequest;
import com.example.ead_backend.dto.service.UpdateServiceRequest;
import com.example.ead_backend.model.entity.Service;
import org.springframework.stereotype.Component;

@Component
public class ServiceMapper {
    
    public Service toEntity(CreateServiceRequest request) {
        return Service.builder()
                .name(request.getName())
                .description(request.getDescription())
                .durationInHours(request.getDurationInHours())
                .price(request.getPrice())
                .currency(request.getCurrency() != null ? request.getCurrency() : "LKR")
                .status(request.getStatus())
                .build();
    }
    
    public void updateEntity(Service service, UpdateServiceRequest request) {
        if (request.getName() != null) {
            service.setName(request.getName());
        }
        if (request.getDescription() != null) {
            service.setDescription(request.getDescription());
        }
        if (request.getDurationInHours() != null) {
            service.setDurationInHours(request.getDurationInHours());
        }
        if (request.getPrice() != null) {
            service.setPrice(request.getPrice());
        }
        if (request.getCurrency() != null) {
            service.setCurrency(request.getCurrency());
        }
        if (request.getStatus() != null) {
            service.setStatus(request.getStatus());
        }
    }
}
