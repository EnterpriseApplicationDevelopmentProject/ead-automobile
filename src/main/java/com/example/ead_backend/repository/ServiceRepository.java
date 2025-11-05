package com.example.ead_backend.repository;

import com.example.ead_backend.model.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, String> {
    
    // Find all active services
    List<Service> findByIsActiveTrue();
    
    // Find service by name
    Optional<Service> findByName(String name);
    
    // Find services by active status
    List<Service> findByIsActive(Boolean isActive);
}
