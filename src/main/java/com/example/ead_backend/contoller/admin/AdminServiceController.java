package com.example.ead_backend.contoller.admin;

import com.example.ead_backend.dto.service.CategoryRequest;
import com.example.ead_backend.dto.service.CreateServiceRequest;
import com.example.ead_backend.dto.service.UpdateServiceRequest;
import com.example.ead_backend.dto.common.ApiResponse;
import com.example.ead_backend.dto.service.CategoryResponse;
import com.example.ead_backend.dto.service.ServiceResponse;
import com.example.ead_backend.service.servicemanagement.ServiceCategoryService;
import com.example.ead_backend.service.servicemanagement.ServiceManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for admin service management
 * Author: Member 9 - Dilminda W.W.C.
 */
@RestController
@RequestMapping("/api/admin/services")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Admin - Service Management", description = "APIs for managing automobile services and categories (Admin only)")
@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("hasRole('ADMIN')")
public class AdminServiceController {
    
    private final ServiceManagementService serviceManagementService;
    private final ServiceCategoryService serviceCategoryService;
    
    // ==================== SERVICE MANAGEMENT ENDPOINTS ====================
    
    @PostMapping
    @Operation(
        summary = "Create new service",
        description = "Create a new automobile service. Requires ADMIN role."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Service created successfully",
            content = @Content(schema = @Schema(implementation = ServiceResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid input data"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Access denied - Admin role required"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Category not found"
        )
    })
    public ResponseEntity<ApiResponse<ServiceResponse>> createService(
            @Valid @RequestBody CreateServiceRequest request) {
        
        log.info("Admin creating new service: {}", request.getName());
        ServiceResponse response = serviceManagementService.createService(request);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Service created successfully"));
    }
    
    @GetMapping
    @Operation(
        summary = "Get all services",
        description = "Get all services including inactive ones. Requires ADMIN role."
    )
    public ResponseEntity<ApiResponse<List<ServiceResponse>>> getAllServices() {
        log.info("Admin fetching all services");
        List<ServiceResponse> services = serviceManagementService.getAllServices();
        
        return ResponseEntity.ok(
                ApiResponse.success(services, "Services retrieved successfully")
        );
    }
    
    @GetMapping("/{id}")
    @Operation(
        summary = "Get service by ID",
        description = "Get detailed information about a specific service"
    )
    public ResponseEntity<ApiResponse<ServiceResponse>> getServiceById(
            @Parameter(description = "Service ID") @PathVariable Long id) {
        
        log.info("Admin fetching service with ID: {}", id);
        ServiceResponse service = serviceManagementService.getServiceById(id);
        
        return ResponseEntity.ok(
                ApiResponse.success(service, "Service retrieved successfully")
        );
    }
    
    @PutMapping("/{id}")
    @Operation(
        summary = "Update service",
        description = "Update an existing service. All fields in request are optional."
    )
    public ResponseEntity<ApiResponse<ServiceResponse>> updateService(
            @Parameter(description = "Service ID") @PathVariable Long id,
            @Valid @RequestBody UpdateServiceRequest request) {
        
        log.info("Admin updating service ID: {}", id);
        ServiceResponse response = serviceManagementService.updateService(id, request);
        
        return ResponseEntity.ok(
                ApiResponse.success(response, "Service updated successfully")
        );
    }
    
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete service",
        description = "Delete a service. Cannot delete if service has existing appointments."
    )
    public ResponseEntity<ApiResponse<Void>> deleteService(
            @Parameter(description = "Service ID") @PathVariable Long id) {
        
        log.info("Admin deleting service ID: {}", id);
        serviceManagementService.deleteService(id);
        
        return ResponseEntity.ok(
                ApiResponse.success(null, "Service deleted successfully")
        );
    }
    
    @PatchMapping("/{id}/toggle-status")
    @Operation(
        summary = "Toggle service status",
        description = "Activate or deactivate a service"
    )
    public ResponseEntity<ApiResponse<ServiceResponse>> toggleServiceStatus(
            @Parameter(description = "Service ID") @PathVariable Long id) {
        
        log.info("Admin toggling status for service ID: {}", id);
        ServiceResponse response = serviceManagementService.toggleServiceStatus(id);
        
        return ResponseEntity.ok(
                ApiResponse.success(
                    response, 
                    "Service status changed to: " + (response.isActive() ? "Active" : "Inactive")
                )
        );
    }
    
    @GetMapping("/category/{categoryId}")
    @Operation(
        summary = "Get services by category",
        description = "Get all services in a specific category including inactive ones"
    )
    public ResponseEntity<ApiResponse<List<ServiceResponse>>> getServicesByCategory(
            @Parameter(description = "Category ID") @PathVariable Long categoryId) {
        
        log.info("Admin fetching services for category ID: {}", categoryId);
        List<ServiceResponse> services = serviceManagementService.getAllServicesByCategory(categoryId);
        
        return ResponseEntity.ok(
                ApiResponse.success(services, "Services retrieved successfully")
        );
    }
    
    // ==================== CATEGORY MANAGEMENT ENDPOINTS ====================
    
    @PostMapping("/categories")
    @Operation(
        summary = "Create new category",
        description = "Create a new service category"
    )
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CategoryRequest request) {
        
        log.info("Admin creating new category: {}", request.getName());
        CategoryResponse response = serviceCategoryService.createCategory(request);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Category created successfully"));
    }
    
    @GetMapping("/categories")
    @Operation(
        summary = "Get all categories",
        description = "Get all service categories including inactive ones"
    )
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories(
            @Parameter(description = "Optional search keyword") 
            @RequestParam(required = false) String search) {
        
        log.info("Admin fetching categories with search: {}", search);
        List<CategoryResponse> categories;
        
        if (search != null && !search.trim().isEmpty()) {
            categories = serviceCategoryService.searchCategories(search);
        } else {
            categories = serviceCategoryService.getAllCategories();
        }
        
        return ResponseEntity.ok(
                ApiResponse.success(categories, "Categories retrieved successfully")
        );
    }
    
    @GetMapping("/categories/{id}")
    @Operation(
        summary = "Get category by ID",
        description = "Get detailed information about a specific category"
    )
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(
            @Parameter(description = "Category ID") @PathVariable Long id) {
        
        log.info("Admin fetching category with ID: {}", id);
        CategoryResponse category = serviceCategoryService.getCategoryById(id);
        
        return ResponseEntity.ok(
                ApiResponse.success(category, "Category retrieved successfully")
        );
    }
    
    @PutMapping("/categories/{id}")
    @Operation(
        summary = "Update category",
        description = "Update an existing service category"
    )
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @Parameter(description = "Category ID") @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {
        
        log.info("Admin updating category ID: {}", id);
        CategoryResponse response = serviceCategoryService.updateCategory(id, request);
        
        return ResponseEntity.ok(
                ApiResponse.success(response, "Category updated successfully")
        );
    }
    
    @DeleteMapping("/categories/{id}")
    @Operation(
        summary = "Delete category",
        description = "Delete a category. Cannot delete if category has services."
    )
    public ResponseEntity<ApiResponse<Void>> deleteCategory(
            @Parameter(description = "Category ID") @PathVariable Long id) {
        
        log.info("Admin deleting category ID: {}", id);
        serviceCategoryService.deleteCategory(id);
        
        return ResponseEntity.ok(
                ApiResponse.success(null, "Category deleted successfully")
        );
    }
    
    @GetMapping("/categories/active")
    @Operation(
        summary = "Get active categories",
        description = "Get only active service categories"
    )
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getActiveCategories() {
        log.info("Admin fetching active categories");
        List<CategoryResponse> categories = serviceCategoryService.getActiveCategories();
        
        return ResponseEntity.ok(
                ApiResponse.success(categories, "Active categories retrieved successfully")
        );
    }
}