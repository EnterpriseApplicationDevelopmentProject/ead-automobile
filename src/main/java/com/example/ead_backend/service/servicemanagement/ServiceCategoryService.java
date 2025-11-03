package com.example.ead_backend.service.servicemanagement;

import com.example.ead_backend.dto.service.CategoryRequest;
import com.example.ead_backend.dto.service.CategoryResponse;

import java.util.List;

/**
 * Service interface for managing service categories
 * Author: Member 9 - Dilminda W.W.C.
 */
public interface ServiceCategoryService {
    
    /**
     * Create a new service category
     * @param request Category details
     * @return Created category
     * @throws BadRequestException if category name already exists
     */
    CategoryResponse createCategory(CategoryRequest request);
    
    /**
     * Update an existing category
     * @param id Category ID
     * @param request Updated category details
     * @return Updated category
     * @throws ResourceNotFoundException if category not found
     * @throws BadRequestException if new name conflicts with existing category
     */
    CategoryResponse updateCategory(Long id, CategoryRequest request);
    
    /**
     * Delete a category
     * @param id Category ID
     * @throws ResourceNotFoundException if category not found
     * @throws BadRequestException if category has services
     */
    void deleteCategory(Long id);
    
    /**
     * Get category by ID
     * @param id Category ID
     * @return Category details
     * @throws ResourceNotFoundException if category not found
     */
    CategoryResponse getCategoryById(Long id);
    
    /**
     * Get all categories
     * @return List of all categories
     */
    List<CategoryResponse> getAllCategories();
    
    /**
     * Get only active categories
     * @return List of active categories
     */
    List<CategoryResponse> getActiveCategories();
    
    /**
     * Search categories by name
     * @param keyword Search keyword
     * @return List of matching categories
     */
    List<CategoryResponse> searchCategories(String keyword);
}