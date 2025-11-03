package com.example.ead_backend.repository;

import com.example.ead_backend.model.entity.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for ServiceCategory entity
 * Author: Member 9 - Dilminda W.W.C.
 */
@Repository
public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Long> {
    
    /**
     * Find category by name (case-insensitive)
     */
    Optional<ServiceCategory> findByNameIgnoreCase(String name);
    
    /**
     * Find category by exact name
     */
    Optional<ServiceCategory> findByName(String name);
    
    /**
     * Check if category exists by name (case-insensitive)
     */
    boolean existsByNameIgnoreCase(String name);
    
    /**
     * Check if category exists by exact name
     */
    boolean existsByName(String name);
    
    /**
     * Find all active categories
     */
    List<ServiceCategory> findByIsActiveTrue();
    
    /**
     * Find categories by active status
     */
    List<ServiceCategory> findByIsActive(boolean isActive);
    
    /**
     * Find categories by name containing keyword (case-insensitive)
     */
    List<ServiceCategory> findByNameContainingIgnoreCase(String keyword);
    
    /**
     * Count services in a category
     */
    @Query("SELECT COUNT(s) FROM Service s WHERE s.category.id = :categoryId")
    long countServicesByCategoryId(@Param("categoryId") Long categoryId);
    
    /**
     * Find categories with services count
     */
    @Query("SELECT c FROM ServiceCategory c LEFT JOIN FETCH c.services WHERE c.id = :id")
    Optional<ServiceCategory> findByIdWithServices(@Param("id") Long id);
}