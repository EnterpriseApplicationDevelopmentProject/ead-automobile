package com.example.ead_backend.service.impl;

import com.example.ead_backend.dto.ServiceDTO;
import com.example.ead_backend.mapper.ServiceMapper;
import com.example.ead_backend.model.entity.Service;
import com.example.ead_backend.repository.ServiceRepository;
import com.example.ead_backend.service.CloudinaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceServiceImplTest {

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private ServiceMapper serviceMapper;

    @Mock
    private CloudinaryService cloudinaryService;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private ServiceServiceImpl serviceService;

    private ServiceDTO serviceDTO;
    private Service service;

    @BeforeEach
    void setUp() {
        serviceDTO = new ServiceDTO();
        serviceDTO.setId(1L);
        serviceDTO.setName("Oil Change");
        serviceDTO.setDescription("Complete oil change service");
        serviceDTO.setPrice(BigDecimal.valueOf(49.99));
        serviceDTO.setEstimatedDurationMinutes(60);
        serviceDTO.setActive(true);

        service = new Service();
        service.setId(1L);
        service.setName("Oil Change");
        service.setDescription("Complete oil change service");
        service.setPrice(BigDecimal.valueOf(49.99));
        service.setEstimatedDurationMinutes(60);
        service.setActive(true);
    }

    @Test
    void testCreateService_Success() {
        // Given
        when(serviceRepository.existsByNameIgnoreCase("Oil Change")).thenReturn(false);
        when(serviceMapper.toEntity(serviceDTO)).thenReturn(service);
        when(serviceRepository.save(service)).thenReturn(service);
        when(serviceMapper.toDTO(service)).thenReturn(serviceDTO);

        // When
        ServiceDTO result = serviceService.createService(serviceDTO);

        // Then
        assertNotNull(result);
        assertEquals("Oil Change", result.getName());
        assertEquals(BigDecimal.valueOf(49.99), result.getPrice());
        verify(serviceRepository).save(service);
    }

    @Test
    void testCreateService_DuplicateName() {
        // Given
        when(serviceRepository.existsByNameIgnoreCase("Oil Change")).thenReturn(true);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> serviceService.createService(serviceDTO));
        assertTrue(exception.getMessage().contains("already exists"));
        verify(serviceRepository, never()).save(any());
    }

    @Test
    void testCreateServiceWithImage_Success() throws IOException {
        // Given
        Map<String, Object> uploadResult = new HashMap<>();
        uploadResult.put("secure_url", "https://cloudinary.com/image.jpg");
        uploadResult.put("public_id", "service_12345");

        when(serviceRepository.existsByNameIgnoreCase("Oil Change")).thenReturn(false);
        when(cloudinaryService.uploadImage(multipartFile)).thenReturn(uploadResult);
        when(serviceMapper.toEntity(serviceDTO)).thenReturn(service);
        when(serviceRepository.save(any(Service.class))).thenReturn(service);
        when(serviceMapper.toDTO(service)).thenReturn(serviceDTO);

        // When
        ServiceDTO result = serviceService.createServiceWithImage(serviceDTO, multipartFile);

        // Then
        assertNotNull(result);
        verify(cloudinaryService).uploadImage(multipartFile);
        verify(serviceRepository).save(any(Service.class));
    }

    @Test
    void testGetServiceById_Success() {
        // Given
        when(serviceRepository.findById(1L)).thenReturn(Optional.of(service));
        when(serviceMapper.toDTO(service)).thenReturn(serviceDTO);

        // When
        ServiceDTO result = serviceService.getServiceById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Oil Change", result.getName());
        verify(serviceRepository).findById(1L);
    }

    @Test
    void testGetServiceById_NotFound() {
        // Given
        when(serviceRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> serviceService.getServiceById(999L));
    }

    @Test
    void testGetAllServices() {
        // Given
        List<Service> services = Arrays.asList(service);
        when(serviceRepository.findAll()).thenReturn(services);
        when(serviceMapper.toDTO(service)).thenReturn(serviceDTO);

        // When
        List<ServiceDTO> result = serviceService.getAllServices();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(serviceRepository).findAll();
    }

    @Test
    void testGetActiveServices() {
        // Given
        List<Service> activeServices = Arrays.asList(service);
        when(serviceRepository.findByActiveTrue()).thenReturn(activeServices);
        when(serviceMapper.toDTO(service)).thenReturn(serviceDTO);

        // When
        List<ServiceDTO> result = serviceService.getActiveServices();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getActive());
        verify(serviceRepository).findByActiveTrue();
    }

    @Test
    void testUpdateService_Success() {
        // Given
        ServiceDTO updateDTO = new ServiceDTO();
        updateDTO.setName("Oil Change");
        updateDTO.setDescription("Updated description");
        updateDTO.setPrice(BigDecimal.valueOf(59.99));
        updateDTO.setEstimatedDurationMinutes(90);
        updateDTO.setActive(true);

        when(serviceRepository.findById(1L)).thenReturn(Optional.of(service));
        when(serviceRepository.save(service)).thenReturn(service);
        when(serviceMapper.toDTO(service)).thenReturn(updateDTO);

        // When
        ServiceDTO result = serviceService.updateService(1L, updateDTO);

        // Then
        assertNotNull(result);
        verify(serviceRepository).save(service);
    }

    @Test
    void testUpdateService_NameConflict() {
        // Given
        service.setName("Old Name");
        ServiceDTO updateDTO = new ServiceDTO();
        updateDTO.setName("Existing Service");

        when(serviceRepository.findById(1L)).thenReturn(Optional.of(service));
        when(serviceRepository.existsByNameIgnoreCase("Existing Service")).thenReturn(true);

        // When & Then
        assertThrows(RuntimeException.class,
                () -> serviceService.updateService(1L, updateDTO));
        verify(serviceRepository, never()).save(any());
    }

    @Test
    void testUpdateServiceWithImage_Success() throws IOException {
        // Given
        Map<String, Object> uploadResult = new HashMap<>();
        uploadResult.put("secure_url", "https://cloudinary.com/new-image.jpg");
        uploadResult.put("public_id", "service_67890");

        service.setImagePublicId("old_public_id");

        when(serviceRepository.findById(1L)).thenReturn(Optional.of(service));
        when(multipartFile.isEmpty()).thenReturn(false);
        when(cloudinaryService.updateImage(multipartFile, "old_public_id")).thenReturn(uploadResult);
        when(serviceRepository.save(service)).thenReturn(service);
        when(serviceMapper.toDTO(service)).thenReturn(serviceDTO);

        // When
        ServiceDTO result = serviceService.updateServiceWithImage(1L, serviceDTO, multipartFile);

        // Then
        assertNotNull(result);
        verify(cloudinaryService).updateImage(multipartFile, "old_public_id");
        verify(serviceRepository).save(service);
    }

    @Test
    void testDeleteService_Success() throws IOException {
        // Given
        service.setImagePublicId("service_12345");
        when(serviceRepository.findById(1L)).thenReturn(Optional.of(service));
        doNothing().when(cloudinaryService).deleteImage("service_12345");

        // When
        serviceService.deleteService(1L);

        // Then
        verify(cloudinaryService).deleteImage("service_12345");
        verify(serviceRepository).deleteById(1L);
    }

    @Test
    void testDeleteService_NoImage() throws IOException {
        // Given
        service.setImagePublicId(null);
        when(serviceRepository.findById(1L)).thenReturn(Optional.of(service));

        // When
        serviceService.deleteService(1L);

        // Then
        verify(cloudinaryService, never()).deleteImage(anyString());
        verify(serviceRepository).deleteById(1L);
    }

    @Test
    void testDeleteService_NotFound() {
        // Given
        when(serviceRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> serviceService.deleteService(999L));
        verify(serviceRepository, never()).deleteById(anyLong());
    }

    @Test
    void testToggleServiceStatus() {
        // Given
        service.setActive(true);
        when(serviceRepository.findById(1L)).thenReturn(Optional.of(service));
        when(serviceRepository.save(service)).thenReturn(service);
        when(serviceMapper.toDTO(service)).thenReturn(serviceDTO);

        // When
        ServiceDTO result = serviceService.toggleServiceStatus(1L);

        // Then
        assertNotNull(result);
        verify(serviceRepository).save(service);
    }
}
