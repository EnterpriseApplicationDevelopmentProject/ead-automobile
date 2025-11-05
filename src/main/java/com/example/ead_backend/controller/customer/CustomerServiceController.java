package com.example.ead_backend.controller.customer;

import com.example.ead_backend.dto.ServiceDTO;
import com.example.ead_backend.service.ServiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Customer controller for viewing available services
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/customer/services")
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceController {

    private final ServiceService serviceService;

    /**
     * Get all active services (for customers to view when booking appointments)
     */
    @GetMapping
    public ResponseEntity<?> getAllActiveServices() {
        try {
            log.info("Customer: Fetching all active services");
            List<ServiceDTO> services = serviceService.getAllActiveServices();
            return ResponseEntity.ok(services);
        } catch (Exception e) {
            log.error("Error fetching active services: {}", e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch services: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Get service by ID (for customers to view service details)
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getServiceById(@PathVariable String id) {
        try {
            log.info("Customer: Fetching service by ID: {}", id);
            ServiceDTO service = serviceService.getServiceById(id);
            
            // Only return if service is active
            if (!service.getIsActive()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Service not available");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            
            return ResponseEntity.ok(service);
        } catch (RuntimeException e) {
            log.error("Service not found: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Service not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            log.error("Error fetching service: {}", e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch service: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
