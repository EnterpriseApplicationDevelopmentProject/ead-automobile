package com.example.ead_backend.controller.admin;

import com.example.ead_backend.dto.ServiceDTO;
import com.example.ead_backend.service.ServiceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/admin/services")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@Slf4j
public class AdminServiceController {

    private final ServiceService serviceService;
    private final ObjectMapper objectMapper;

    /**
     * Create a new service without image
     */
    @PostMapping
    public ResponseEntity<?> createService(@Valid @RequestBody ServiceDTO serviceDTO) {
        try {
            log.info("Creating new service: {}", serviceDTO.getName());
            ServiceDTO created = serviceService.createService(serviceDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            log.error("Validation error creating service: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            log.error("Error creating service: {}", e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to create service: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Create a new service with image
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createServiceWithImage(
            @RequestParam("service") String serviceDTOJson,
            @RequestParam("image") MultipartFile image) {
        try {
            log.info("Creating new service with image");
            
            // Parse JSON to ServiceDTO
            ServiceDTO serviceDTO = objectMapper.readValue(serviceDTOJson, ServiceDTO.class);
            
            // Validate image
            if (image.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Image file is required");
                return ResponseEntity.badRequest().body(error);
            }
            
            ServiceDTO created = serviceService.createServiceWithImage(serviceDTO, image);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            log.error("Validation error creating service: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (IOException e) {
            log.error("Error uploading image: {}", e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to upload image: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        } catch (Exception e) {
            log.error("Error creating service: {}", e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to create service: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Get service by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getServiceById(@PathVariable String id) {
        try {
            log.info("Fetching service by ID: {}", id);
            ServiceDTO service = serviceService.getServiceById(id);
            return ResponseEntity.ok(service);
        } catch (RuntimeException e) {
            log.error("Service not found: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            log.error("Error fetching service: {}", e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch service: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Get all services (admin view - includes inactive)
     */
    @GetMapping
    public ResponseEntity<?> getAllServices() {
        try {
            log.info("Fetching all services");
            List<ServiceDTO> services = serviceService.getAllServices();
            return ResponseEntity.ok(services);
        } catch (Exception e) {
            log.error("Error fetching services: {}", e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch services: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Get all active services (customer view - only active services)
     */
    @GetMapping("/active")
    @PreAuthorize("permitAll()") // Allow customers to view active services
    public ResponseEntity<?> getAllActiveServices() {
        try {
            log.info("Fetching all active services");
            List<ServiceDTO> services = serviceService.getAllActiveServices();
            return ResponseEntity.ok(services);
        } catch (Exception e) {
            log.error("Error fetching active services: {}", e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch active services: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Update service without changing image
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateService(
            @PathVariable String id,
            @Valid @RequestBody ServiceDTO serviceDTO) {
        try {
            log.info("Updating service: {}", id);
            ServiceDTO updated = serviceService.updateService(id, serviceDTO);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            log.error("Validation error updating service: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (RuntimeException e) {
            log.error("Service not found: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            log.error("Error updating service: {}", e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update service: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Update service with new image
     */
    @PutMapping(value = "/{id}/with-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateServiceWithImage(
            @PathVariable String id,
            @RequestParam("service") String serviceDTOJson,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            log.info("Updating service with image: {}", id);
            
            // Parse JSON to ServiceDTO
            ServiceDTO serviceDTO = objectMapper.readValue(serviceDTOJson, ServiceDTO.class);
            
            ServiceDTO updated = serviceService.updateServiceWithImage(id, serviceDTO, image);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            log.error("Validation error updating service: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (RuntimeException e) {
            log.error("Service not found: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (IOException e) {
            log.error("Error uploading image: {}", e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to upload image: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        } catch (Exception e) {
            log.error("Error updating service: {}", e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update service: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Soft delete service (set isActive to false)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteService(@PathVariable String id) {
        try {
            log.info("Soft deleting service: {}", id);
            serviceService.deleteService(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Service deactivated successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Service not found: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            log.error("Error deleting service: {}", e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to delete service: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Permanently delete service from database
     */
    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<?> permanentlyDeleteService(@PathVariable String id) {
        try {
            log.info("Permanently deleting service: {}", id);
            serviceService.permanentlyDeleteService(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Service permanently deleted successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Service not found: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (IOException e) {
            log.error("Error deleting image: {}", e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to delete service image: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        } catch (Exception e) {
            log.error("Error deleting service: {}", e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to permanently delete service: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Toggle service active status
     */
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<?> toggleServiceStatus(@PathVariable String id) {
        try {
            log.info("Toggling service status: {}", id);
            ServiceDTO updated = serviceService.toggleServiceStatus(id);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            log.error("Service not found: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            log.error("Error toggling service status: {}", e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to toggle service status: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
