package com.example.ead_backend.repository;

import com.example.ead_backend.model.entity.Service;
import com.example.ead_backend.model.enums.ServiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    
    // Search services by name or description
    @Query("SELECT s FROM Service s WHERE " +
           "LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Service> searchServices(@Param("keyword") String keyword, Pageable pageable);
    
    // Filter by category
    Page<Service> findByCategoryId(Long categoryId, Pageable pageable);
    
    // Filter by price range
    Page<Service> findByPriceBetween(Double minPrice, Double maxPrice, Pageable pageable);
    
    // Filter by category and price range
    @Query("SELECT s FROM Service s WHERE s.category.id = :categoryId AND s.price BETWEEN :minPrice AND :maxPrice")
    Page<Service> findByCategoryIdAndPriceBetween(
        @Param("categoryId") Long categoryId,
        @Param("minPrice") Double minPrice,
        @Param("maxPrice") Double maxPrice,
        Pageable pageable
    );
    
    // Filter by status
    Page<Service> findByStatus(ServiceStatus status, Pageable pageable);
    
    // Find available services (ACTIVE status)
    @Query("SELECT s FROM Service s WHERE s.status = 'ACTIVE'")
    Page<Service> findAvailableServices(Pageable pageable);
    
    // Check if service is available
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Service s WHERE s.id = :serviceId AND s.status = 'ACTIVE'")
    boolean isServiceAvailable(@Param("serviceId") Long serviceId);
}
