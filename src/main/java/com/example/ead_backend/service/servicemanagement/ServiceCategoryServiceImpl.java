package com.example.ead_backend.service.servicemanagement;

import com.example.ead_backend.dto.service.CategoryRequest;
import com.example.ead_backend.dto.service.CategoryResponse;
import com.example.ead_backend.exception.BadRequestException;
import com.example.ead_backend.exception.ResourceNotFoundException;
import com.example.ead_backend.model.entity.ServiceCategory;
import com.example.ead_backend.repository.ServiceCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of ServiceCategoryService
 * Author: Member 9 - Dilminda W.W.C.
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ServiceCategoryServiceImpl implements ServiceCategoryService {
    
    private final ServiceCategoryRepository categoryRepository;
    
    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        log.info("Creating new category: {}", request.getName());
        
        // Check if category already exists (case-insensitive)
        if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
            log.warn("Category creation failed: Category with name '{}' already exists", request.getName());
            throw new BadRequestException("Category with name '" + request.getName() + "' already exists");
        }
        
        // Create new category
        ServiceCategory category = ServiceCategory.builder()
                .name(request.getName().trim())
                .description(request.getDescription())
                .isActive(request.isActive())
                .build();
        
        ServiceCategory saved = categoryRepository.save(category);
        log.info("Category created successfully with ID: {}", saved.getId());
        
        return mapToResponse(saved);
    }
    
    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        log.info("Updating category ID: {}", id);
        
        // Find existing category
        ServiceCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Category not found with ID: {}", id);
                    return new ResourceNotFoundException("Category not found with ID: " + id);
                });
        
        // Check if new name conflicts with existing category (excluding current category)
        String newName = request.getName().trim();
        if (!category.getName().equalsIgnoreCase(newName) && 
            categoryRepository.existsByNameIgnoreCase(newName)) {
            log.warn("Category update failed: Category with name '{}' already exists", newName);
            throw new BadRequestException("Category with name '" + newName + "' already exists");
        }
        
        // Update fields
        category.setName(newName);
        category.setDescription(request.getDescription());
        category.setActive(request.isActive());
        
        ServiceCategory updated = categoryRepository.save(category);
        log.info("Category updated successfully: {}", id);
        
        return mapToResponse(updated);
    }
    
    @Override
    public void deleteCategory(Long id) {
        log.info("Attempting to delete category ID: {}", id);
        
        // Find category
        ServiceCategory category = categoryRepository.findByIdWithServices(id)
                .orElseThrow(() -> {
                    log.error("Category not found with ID: {}", id);
                    return new ResourceNotFoundException("Category not found with ID: " + id);
                });
        
        // Check if category has services
        if (category.getServices() != null && !category.getServices().isEmpty()) {
            log.warn("Cannot delete category ID: {} - has {} services", id, category.getServices().size());
            throw new BadRequestException(
                "Cannot delete category with existing services. " +
                "Please delete or reassign " + category.getServices().size() + " service(s) first."
            );
        }
        
        categoryRepository.delete(category);
        log.info("Category deleted successfully: {}", id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        log.debug("Fetching category with ID: {}", id);
        
        ServiceCategory category = categoryRepository.findByIdWithServices(id)
                .orElseThrow(() -> {
                    log.error("Category not found with ID: {}", id);
                    return new ResourceNotFoundException("Category not found with ID: " + id);
                });
        
        return mapToResponse(category);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        log.debug("Fetching all categories");
        
        List<ServiceCategory> categories = categoryRepository.findAll();
        log.info("Found {} categories", categories.size());
        
        return categories.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getActiveCategories() {
        log.debug("Fetching active categories");
        
        List<ServiceCategory> categories = categoryRepository.findByIsActiveTrue();
        log.info("Found {} active categories", categories.size());
        
        return categories.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> searchCategories(String keyword) {
        log.debug("Searching categories with keyword: {}", keyword);
        
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllCategories();
        }
        
        List<ServiceCategory> categories = categoryRepository.findByNameContainingIgnoreCase(keyword.trim());
        log.info("Found {} categories matching '{}'", categories.size(), keyword);
        
        return categories.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Helper method to map entity to response DTO
     */
    private CategoryResponse mapToResponse(ServiceCategory category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .isActive(category.isActive())
                .serviceCount(category.getServices() != null ? category.getServices().size() : 0)
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}