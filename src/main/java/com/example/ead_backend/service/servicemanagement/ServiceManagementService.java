package com.example.ead_backend.service.servicemanagement;

import com.example.ead_backend.dto.service.CreateServiceRequest;
import com.example.ead_backend.dto.service.UpdateServiceRequest;
import com.example.ead_backend.dto.service.ServiceResponse;

import java.util.List;

/**
 * Service interface for managing automobile services
 * Member 3 created customer methods, Member 9 adds admin methods
 */
public interface ServiceManagementService {
    
    // ==================== CUSTOMER METHODS (Member 3) ====================
    
    /**
     * Get all active services (for customers)
     */
    List<ServiceResponse> getAllActiveServices();
    
    /**
     * Get service by ID
     */
    ServiceResponse getServiceById(Long id);
    
    /**
     * Search services by keyword
     */
    List<ServiceResponse> searchServices(String keyword);
    
    /**
     * Get services by category
     */
    List<ServiceResponse> getServicesByCategory(Long categoryId);
    
    /**
     * Filter services by price range
     */
    List<ServiceResponse> getServicesByPriceRange(Double minPrice, Double maxPrice);
    
    // ==================== ADMIN METHODS (Member 9) ====================
    
    /**
     * Create a new service (Admin only)
     * @param request Service details
     * @return Created service
     * @throws ResourceNotFoundException if category not found
     */
    ServiceResponse createService(CreateServiceRequest request);
    
    /**
     * Update an existing service (Admin only)
     * @param id Service ID
     * @param request Updated service details
     * @return Updated service
     * @throws ResourceNotFoundException if service or category not found
     */
    ServiceResponse updateService(Long id, UpdateServiceRequest request);
    
    /**
     * Delete a service (Admin only)
     * @param id Service ID
     * @throws ResourceNotFoundException if service not found
     * @throws BadRequestException if service has appointments
     */
    void deleteService(Long id);
    
    /**
     * Get all services including inactive (Admin only)
     * @return List of all services
     */
    List<ServiceResponse> getAllServices();
    
    /**
     * Toggle service active status (Admin only)
     * @param id Service ID
     * @return Updated service
     * @throws ResourceNotFoundException if service not found
     */
    ServiceResponse toggleServiceStatus(Long id);
    
    /**
     * Get services by category including inactive (Admin only)
     * @param categoryId Category ID
     * @return List of services in category
     */
    List<ServiceResponse> getAllServicesByCategory(Long categoryId);
}