package com.example.ead_backend.service.servicemanagement;

import com.example.ead_backend.model.entity.Service;
import com.example.ead_backend.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceManagementServiceImpl implements ServiceManagementService {
    
    private final ServiceRepository serviceRepository;
    
    @Override
    public Page<Service> getAllServices(Pageable pageable) {
        return serviceRepository.findAll(pageable);
    }
    
    @Override
    public Service getServiceById(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));
    }
    
    @Override
    public Page<Service> searchServices(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllServices(pageable);
        }
        return serviceRepository.searchServices(keyword, pageable);
    }
    
    @Override
    public Page<Service> filterByCategory(Long categoryId, Pageable pageable) {
        return serviceRepository.findByCategoryId(categoryId, pageable);
    }
    
    @Override
    public Page<Service> filterByPriceRange(Double minPrice, Double maxPrice, Pageable pageable) {
        return serviceRepository.findByPriceBetween(minPrice, maxPrice, pageable);
    }
    
    @Override
    public Page<Service> filterByCategoryAndPrice(Long categoryId, Double minPrice, Double maxPrice, Pageable pageable) {
        return serviceRepository.findByCategoryIdAndPriceBetween(categoryId, minPrice, maxPrice, pageable);
    }
    
    @Override
    public Page<Service> getAvailableServices(Pageable pageable) {
        return serviceRepository.findAvailableServices(pageable);
    }
    
    @Override
    public boolean isServiceAvailable(Long serviceId) {
        return serviceRepository.isServiceAvailable(serviceId);
    }
}
