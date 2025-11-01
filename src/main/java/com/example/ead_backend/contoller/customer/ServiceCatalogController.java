package com.example.ead_backend.contoller.customer;

import com.example.ead_backend.model.entity.Service;
import com.example.ead_backend.service.servicemanagement.ServiceManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/customer/services")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})  // ← Add this line!
@RequiredArgsConstructor
public class ServiceCatalogController {
    
    private final ServiceManagementService serviceManagementService;
    
    /**
     * Get all services with pagination
     * GET /api/customer/services?page=0&size=10&sort=name,asc
     */
    @GetMapping
    public ResponseEntity<Page<Service>> getAllServices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Service> services = serviceManagementService.getAllServices(pageable);
        
        return ResponseEntity.ok(services);
    }
    
    /**
     * Get service by ID
     * GET /api/customer/services/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Service> getServiceById(@PathVariable Long id) {
        Service service = serviceManagementService.getServiceById(id);
        return ResponseEntity.ok(service);
    }
    
    /**
     * Search services by keyword
     * GET /api/customer/services/search?keyword=oil&page=0&size=10
     */
    @GetMapping("/search")
    public ResponseEntity<Page<Service>> searchServices(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Service> services = serviceManagementService.searchServices(keyword, pageable);
        
        return ResponseEntity.ok(services);
    }
    
    /**
     * Filter services by category and/or price range
     * GET /api/customer/services/filter?categoryId=1&minPrice=100&maxPrice=500&page=0&size=10
     */
    @GetMapping("/filter")
    public ResponseEntity<Page<Service>> filterServices(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Service> services;
        
        // Filter by category and price range
        if (categoryId != null && minPrice != null && maxPrice != null) {
            services = serviceManagementService.filterByCategoryAndPrice(categoryId, minPrice, maxPrice, pageable);
        }
        // Filter by category only
        else if (categoryId != null) {
            services = serviceManagementService.filterByCategory(categoryId, pageable);
        }
        // Filter by price range only
        else if (minPrice != null && maxPrice != null) {
            services = serviceManagementService.filterByPriceRange(minPrice, maxPrice, pageable);
        }
        // No filters, return all services
        else {
            services = serviceManagementService.getAllServices(pageable);
        }
        
        return ResponseEntity.ok(services);
    }
    
    /**
     * Get available services (ACTIVE status only)
     * GET /api/customer/services/available?page=0&size=10
     */
    @GetMapping("/available")
    public ResponseEntity<Page<Service>> getAvailableServices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Service> services = serviceManagementService.getAvailableServices(pageable);
        
        return ResponseEntity.ok(services);
    }
    
    /**
     * Check service availability
     * GET /api/customer/services/{id}/availability
     */
    @GetMapping("/{id}/availability")
    public ResponseEntity<Map<String, Object>> checkServiceAvailability(@PathVariable Long id) {
        boolean isAvailable = serviceManagementService.isServiceAvailable(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("serviceId", id);
        response.put("available", isAvailable);
        response.put("message", isAvailable 
                ? "Service is available for booking" 
                : "Service is currently unavailable");
        
        return ResponseEntity.ok(response);
    }
}
