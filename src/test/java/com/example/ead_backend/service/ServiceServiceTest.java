package com.example.ead_backend.service;

import com.example.ead_backend.dto.ServiceDTO;
import com.example.ead_backend.mapper.ServiceMapper;
import com.example.ead_backend.repository.ServiceRepository;
import com.example.ead_backend.service.impl.ServiceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceServiceTest {

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private ServiceMapper serviceMapper;

    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private ServiceServiceImpl serviceService;

    private com.example.ead_backend.model.entity.Service serviceEntity;
    private ServiceDTO serviceDTO;

    @BeforeEach
    void setUp() {
        serviceEntity = com.example.ead_backend.model.entity.Service.builder()
                .id("test-uuid")
                .name("Oil Change")
                .description("Complete oil change service")
                .price(new BigDecimal("49.99"))
                .estimatedDurationMinutes(30)
                .isActive(true)
                .build();

        serviceDTO = ServiceDTO.builder()
                .id("test-uuid")
                .name("Oil Change")
                .description("Complete oil change service")
                .price(new BigDecimal("49.99"))
                .estimatedDurationMinutes(30)
                .isActive(true)
                .build();
    }

    @Test
    void testCreateService_Success() {
        // Arrange
        when(serviceRepository.findByName("Oil Change")).thenReturn(Optional.empty());
        when(serviceMapper.toEntity(any(ServiceDTO.class))).thenReturn(serviceEntity);
        when(serviceRepository.save(any(com.example.ead_backend.model.entity.Service.class)))
                .thenReturn(serviceEntity);
        when(serviceMapper.toDTO(any(com.example.ead_backend.model.entity.Service.class)))
                .thenReturn(serviceDTO);

        // Act
        ServiceDTO result = serviceService.createService(serviceDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Oil Change", result.getName());
        assertEquals(new BigDecimal("49.99"), result.getPrice());
        verify(serviceRepository).save(any(com.example.ead_backend.model.entity.Service.class));
    }

    @Test
    void testCreateService_DuplicateName_ThrowsException() {
        // Arrange
        when(serviceRepository.findByName("Oil Change")).thenReturn(Optional.of(serviceEntity));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> serviceService.createService(serviceDTO)
        );
        assertTrue(exception.getMessage().contains("already exists"));
        verify(serviceRepository, never()).save(any());
    }

    @Test
    void testGetServiceById_Success() {
        // Arrange
        when(serviceRepository.findById("test-uuid")).thenReturn(Optional.of(serviceEntity));
        when(serviceMapper.toDTO(serviceEntity)).thenReturn(serviceDTO);

        // Act
        ServiceDTO result = serviceService.getServiceById("test-uuid");

        // Assert
        assertNotNull(result);
        assertEquals("test-uuid", result.getId());
        assertEquals("Oil Change", result.getName());
    }

    @Test
    void testGetServiceById_NotFound_ThrowsException() {
        // Arrange
        when(serviceRepository.findById("invalid-id")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> serviceService.getServiceById("invalid-id")
        );
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void testGetAllActiveServices_Success() {
        // Arrange
        List<com.example.ead_backend.model.entity.Service> services = Arrays.asList(serviceEntity);
        when(serviceRepository.findByIsActiveTrue()).thenReturn(services);
        when(serviceMapper.toDTO(any(com.example.ead_backend.model.entity.Service.class)))
                .thenReturn(serviceDTO);

        // Act
        List<ServiceDTO> result = serviceService.getAllActiveServices();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Oil Change", result.get(0).getName());
        assertTrue(result.get(0).getIsActive());
    }

    @Test
    void testToggleServiceStatus_Success() {
        // Arrange
        when(serviceRepository.findById("test-uuid")).thenReturn(Optional.of(serviceEntity));
        when(serviceRepository.save(any(com.example.ead_backend.model.entity.Service.class)))
                .thenReturn(serviceEntity);
        when(serviceMapper.toDTO(any(com.example.ead_backend.model.entity.Service.class)))
                .thenReturn(serviceDTO);

        // Act
        ServiceDTO result = serviceService.toggleServiceStatus("test-uuid");

        // Assert
        assertNotNull(result);
        verify(serviceRepository).save(any(com.example.ead_backend.model.entity.Service.class));
    }

    @Test
    void testDeleteService_Success() {
        // Arrange
        when(serviceRepository.findById("test-uuid")).thenReturn(Optional.of(serviceEntity));
        when(serviceRepository.save(any(com.example.ead_backend.model.entity.Service.class)))
                .thenReturn(serviceEntity);

        // Act
        serviceService.deleteService("test-uuid");

        // Assert
        verify(serviceRepository).save(any(com.example.ead_backend.model.entity.Service.class));
    }

    @Test
    void testUpdateService_Success() {
        // Arrange
        ServiceDTO updateDTO = ServiceDTO.builder()
                .name("Premium Oil Change")
                .description("Premium synthetic oil change")
                .price(new BigDecimal("69.99"))
                .estimatedDurationMinutes(45)
                .isActive(true)
                .build();

        when(serviceRepository.findById("test-uuid")).thenReturn(Optional.of(serviceEntity));
        when(serviceRepository.findByName("Premium Oil Change")).thenReturn(Optional.empty());
        when(serviceRepository.save(any(com.example.ead_backend.model.entity.Service.class)))
                .thenReturn(serviceEntity);
        when(serviceMapper.toDTO(any(com.example.ead_backend.model.entity.Service.class)))
                .thenReturn(updateDTO);

        // Act
        ServiceDTO result = serviceService.updateService("test-uuid", updateDTO);

        // Assert
        assertNotNull(result);
        verify(serviceRepository).save(any(com.example.ead_backend.model.entity.Service.class));
    }
}
