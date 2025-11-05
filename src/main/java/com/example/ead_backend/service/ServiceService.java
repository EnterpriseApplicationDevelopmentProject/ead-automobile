package com.example.ead_backend.service;

import com.example.ead_backend.dto.ServiceDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ServiceService {
    
    /**
     * Create a new service without an image
     */
    ServiceDTO createService(ServiceDTO serviceDTO);
    
    /**
     * Create a new service with an image
     */
    ServiceDTO createServiceWithImage(ServiceDTO serviceDTO, MultipartFile image) throws IOException;
    
    /**
     * Get service by ID
     */
    ServiceDTO getServiceById(String id);
    
    /**
     * Get all services
     */
    List<ServiceDTO> getAllServices();
    
    /**
     * Get all active services (for customers)
     */
    List<ServiceDTO> getAllActiveServices();
    
    /**
     * Update service without changing image
     */
    ServiceDTO updateService(String id, ServiceDTO serviceDTO);
    
    /**
     * Update service with a new image
     */
    ServiceDTO updateServiceWithImage(String id, ServiceDTO serviceDTO, MultipartFile image) throws IOException;
    
    /**
     * Delete service (soft delete by setting isActive to false)
     */
    void deleteService(String id);
    
    /**
     * Permanently delete service from database
     */
    void permanentlyDeleteService(String id) throws IOException;
    
    /**
     * Toggle service active status
     */
    ServiceDTO toggleServiceStatus(String id);
}
