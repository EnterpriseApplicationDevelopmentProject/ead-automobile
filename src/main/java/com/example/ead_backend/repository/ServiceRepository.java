package com.example.ead_backend.repository;

import com.example.ead_backend.model.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findByIsActiveTrue();
    List<Service> findByCategoryId(Long categoryId);
    List<Service> findByCategoryIdAndIsActiveTrue(Long categoryId);
}

