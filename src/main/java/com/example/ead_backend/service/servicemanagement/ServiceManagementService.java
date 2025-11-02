package com.example.ead_backend.service.servicemanagement;

import com.example.ead_backend.model.entity.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ServiceManagementService {
    
    // Get all services with pagination
    Page<Service> getAllServices(Pageable pageable);
    
    // Get service by ID
    Service getServiceById(Long id);
    
    // Search services by keyword
    Page<Service> searchServices(String keyword, Pageable pageable);
    
    // Filter services by category
    Page<Service> filterByCategory(Long categoryId, Pageable pageable);
    
    // Filter services by price range
    Page<Service> filterByPriceRange(Double minPrice, Double maxPrice, Pageable pageable);
    
    // Filter services by category and price range
    Page<Service> filterByCategoryAndPrice(Long categoryId, Double minPrice, Double maxPrice, Pageable pageable);
    
    // Get available services (ACTIVE status)
    Page<Service> getAvailableServices(Pageable pageable);
    
    // Check service availability
    boolean isServiceAvailable(Long serviceId);
}
