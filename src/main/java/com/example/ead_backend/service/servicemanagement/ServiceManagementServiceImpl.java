package com.example.ead_backend.service.servicemanagement;

import com.example.ead_backend.dto.service.CreateServiceRequest;
import com.example.ead_backend.dto.service.ServiceResponse;
import com.example.ead_backend.dto.service.UpdateServiceRequest;
import com.example.ead_backend.exception.ResourceNotFoundException;
import com.example.ead_backend.model.entity.Service;
import com.example.ead_backend.model.entity.ServiceCategory;
import com.example.ead_backend.repository.ServiceCategoryRepository;
import com.example.ead_backend.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ServiceManagementServiceImpl implements ServiceManagementService {

    private final ServiceRepository serviceRepository;
    private final ServiceCategoryRepository serviceCategoryRepository;

    @Override
    public List<ServiceResponse> getAllActiveServices() {
        return serviceRepository.findByIsActiveTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ServiceResponse getServiceById(Long id) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + id));
        return mapToResponse(service);
    }

    @Override
    public List<ServiceResponse> searchServices(String keyword) {
        // Simple implementation - you can enhance this
        return serviceRepository.findAll().stream()
                .filter(s -> s.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                        (s.getDescription() != null && s.getDescription().toLowerCase().contains(keyword.toLowerCase())))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceResponse> getServicesByCategory(Long categoryId) {
        return serviceRepository.findByCategoryIdAndIsActiveTrue(categoryId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceResponse> getServicesByPriceRange(Double minPrice, Double maxPrice) {
        return serviceRepository.findAll().stream()
                .filter(s -> s.getIsActive() &&
                        s.getPrice().doubleValue() >= minPrice &&
                        s.getPrice().doubleValue() <= maxPrice)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceResponse> getAllServices() {
        return serviceRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ServiceResponse createService(CreateServiceRequest request) {
        ServiceCategory category = serviceCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Service service = Service.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .estimatedHours(request.getEstimatedHours())
                .category(category)
                .isActive(request.isActive())
                .build();

        Service saved = serviceRepository.save(service);
        log.info("Created new service: {}", saved.getName());
        return mapToResponse(saved);
    }

    @Override
    public ServiceResponse updateService(Long id, UpdateServiceRequest request) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + id));

        if (request.getCategoryId() != null) {
            ServiceCategory category = serviceCategoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            service.setCategory(category);
        }

        if (request.getName() != null) service.setName(request.getName());
        if (request.getDescription() != null) service.setDescription(request.getDescription());
        if (request.getPrice() != null) service.setPrice(request.getPrice());
        if (request.getEstimatedHours() != null) service.setEstimatedHours(request.getEstimatedHours());
        if (request.getIsActive() != null) service.setIsActive(request.getIsActive());

        Service updated = serviceRepository.save(service);
        log.info("Updated service: {}", updated.getName());
        return mapToResponse(updated);
    }

    @Override
    public void deleteService(Long id) {
        if (!serviceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Service not found with id: " + id);
        }
        serviceRepository.deleteById(id);
        log.info("Deleted service with id: {}", id);
    }

    @Override
    public ServiceResponse toggleServiceStatus(Long id) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + id));
        service.setIsActive(!service.getIsActive());
        Service updated = serviceRepository.save(service);
        log.info("Toggled service status for: {} to {}", updated.getName(), updated.getIsActive());
        return mapToResponse(updated);
    }

    @Override
    public List<ServiceResponse> getAllServicesByCategory(Long categoryId) {
        return serviceRepository.findByCategoryId(categoryId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ServiceResponse mapToResponse(Service service) {
        ServiceResponse response = new ServiceResponse();
        response.setId(service.getId());
        response.setServiceName(service.getName());
        response.setDescription(service.getDescription());
        response.setPrice(service.getPrice());
        response.setEstimatedDuration(service.getEstimatedHours() != null ? 
                service.getEstimatedHours().multiply(java.math.BigDecimal.valueOf(60)).intValue() : null);
        response.setCategoryId(service.getCategory().getId());
        response.setCategoryName(service.getCategory().getName());
        response.setIsActive(service.getIsActive());
        return response;
    }
}

