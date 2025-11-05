package com.example.ead_backend.service.impl;

import com.example.ead_backend.dto.ServiceDTO;
import com.example.ead_backend.mapper.ServiceMapper;
import com.example.ead_backend.repository.ServiceRepository;
import com.example.ead_backend.service.CloudinaryService;
import com.example.ead_backend.service.ServiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;
    private final ServiceMapper serviceMapper;
    private final CloudinaryService cloudinaryService;

    @Override
    public ServiceDTO createService(ServiceDTO serviceDTO) {
        log.info("Creating new service: {}", serviceDTO.getName());
        
        // Check if service with same name already exists
        serviceRepository.findByName(serviceDTO.getName())
                .ifPresent(s -> {
                    throw new IllegalArgumentException("Service with name '" + serviceDTO.getName() + "' already exists");
                });
        
        com.example.ead_backend.model.entity.Service service = serviceMapper.toEntity(serviceDTO);
        com.example.ead_backend.model.entity.Service saved = serviceRepository.save(service);
        
        log.info("Service created successfully with ID: {}", saved.getId());
        return serviceMapper.toDTO(saved);
    }

    @Override
    public ServiceDTO createServiceWithImage(ServiceDTO serviceDTO, MultipartFile image) throws IOException {
        log.info("Creating new service with image: {}", serviceDTO.getName());
        
        // Check if service with same name already exists
        serviceRepository.findByName(serviceDTO.getName())
                .ifPresent(s -> {
                    throw new IllegalArgumentException("Service with name '" + serviceDTO.getName() + "' already exists");
                });
        
        // Upload image to Cloudinary
        Map<String, Object> uploadResult = cloudinaryService.uploadServiceImage(image);
        String imageUrl = (String) uploadResult.get("secure_url");
        String publicId = (String) uploadResult.get("public_id");
        
        // Create service entity
        com.example.ead_backend.model.entity.Service service = serviceMapper.toEntity(serviceDTO);
        service.setImageUrl(imageUrl);
        service.setImagePublicId(publicId);
        
        com.example.ead_backend.model.entity.Service saved = serviceRepository.save(service);
        
        log.info("Service created successfully with ID: {} and image", saved.getId());
        return serviceMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceDTO getServiceById(String id) {
        log.info("Fetching service by ID: {}", id);
        
        com.example.ead_backend.model.entity.Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));
        
        return serviceMapper.toDTO(service);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceDTO> getAllServices() {
        log.info("Fetching all services");
        
        return serviceRepository.findAll()
                .stream()
                .map(serviceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceDTO> getAllActiveServices() {
        log.info("Fetching all active services");
        
        return serviceRepository.findByIsActiveTrue()
                .stream()
                .map(serviceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ServiceDTO updateService(String id, ServiceDTO serviceDTO) {
        log.info("Updating service with ID: {}", id);
        
        com.example.ead_backend.model.entity.Service existing = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));
        
        // Check if name is being changed and if new name already exists
        if (!existing.getName().equals(serviceDTO.getName())) {
            serviceRepository.findByName(serviceDTO.getName())
                    .ifPresent(s -> {
                        throw new IllegalArgumentException("Service with name '" + serviceDTO.getName() + "' already exists");
                    });
            existing.setName(serviceDTO.getName());
        }
        
        // Update fields
        existing.setDescription(serviceDTO.getDescription());
        existing.setPrice(serviceDTO.getPrice());
        existing.setEstimatedDurationMinutes(serviceDTO.getEstimatedDurationMinutes());
        if (serviceDTO.getIsActive() != null) {
            existing.setIsActive(serviceDTO.getIsActive());
        }
        
        com.example.ead_backend.model.entity.Service updated = serviceRepository.save(existing);
        
        log.info("Service updated successfully: {}", id);
        return serviceMapper.toDTO(updated);
    }

    @Override
    public ServiceDTO updateServiceWithImage(String id, ServiceDTO serviceDTO, MultipartFile image) throws IOException {
        log.info("Updating service with ID: {} and new image", id);
        
        com.example.ead_backend.model.entity.Service existing = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));
        
        // Update image if provided
        if (image != null && !image.isEmpty()) {
            // Upload new image and delete old one
            Map<String, Object> uploadResult = cloudinaryService.updateImage(image, existing.getImagePublicId());
            String imageUrl = (String) uploadResult.get("secure_url");
            String publicId = (String) uploadResult.get("public_id");
            
            existing.setImageUrl(imageUrl);
            existing.setImagePublicId(publicId);
        }
        
        // Check if name is being changed and if new name already exists
        if (!existing.getName().equals(serviceDTO.getName())) {
            serviceRepository.findByName(serviceDTO.getName())
                    .ifPresent(s -> {
                        throw new IllegalArgumentException("Service with name '" + serviceDTO.getName() + "' already exists");
                    });
            existing.setName(serviceDTO.getName());
        }
        
        // Update fields
        existing.setDescription(serviceDTO.getDescription());
        existing.setPrice(serviceDTO.getPrice());
        existing.setEstimatedDurationMinutes(serviceDTO.getEstimatedDurationMinutes());
        if (serviceDTO.getIsActive() != null) {
            existing.setIsActive(serviceDTO.getIsActive());
        }
        
        com.example.ead_backend.model.entity.Service updated = serviceRepository.save(existing);
        
        log.info("Service updated successfully with new image: {}", id);
        return serviceMapper.toDTO(updated);
    }

    @Override
    public void deleteService(String id) {
        log.info("Soft deleting service with ID: {}", id);
        
        com.example.ead_backend.model.entity.Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));
        
        service.setIsActive(false);
        serviceRepository.save(service);
        
        log.info("Service soft deleted successfully: {}", id);
    }

    @Override
    public void permanentlyDeleteService(String id) throws IOException {
        log.info("Permanently deleting service with ID: {}", id);
        
        com.example.ead_backend.model.entity.Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));
        
        // Delete image from Cloudinary if exists
        if (service.getImagePublicId() != null && !service.getImagePublicId().isEmpty()) {
            try {
                cloudinaryService.deleteImage(service.getImagePublicId());
                log.info("Service image deleted from Cloudinary");
            } catch (IOException e) {
                log.error("Failed to delete service image from Cloudinary: {}", e.getMessage());
                throw new IOException("Failed to delete service image: " + e.getMessage());
            }
        }
        
        serviceRepository.deleteById(id);
        log.info("Service permanently deleted: {}", id);
    }

    @Override
    public ServiceDTO toggleServiceStatus(String id) {
        log.info("Toggling service status for ID: {}", id);
        
        com.example.ead_backend.model.entity.Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));
        
        service.setIsActive(!service.getIsActive());
        com.example.ead_backend.model.entity.Service updated = serviceRepository.save(service);
        
        log.info("Service status toggled to: {}", updated.getIsActive());
        return serviceMapper.toDTO(updated);
    }
}
