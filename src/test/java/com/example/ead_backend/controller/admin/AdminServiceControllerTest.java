package com.example.ead_backend.controller.admin;

import com.example.ead_backend.dto.service.CategoryRequest;
import com.example.ead_backend.dto.service.CreateServiceRequest;
import com.example.ead_backend.dto.service.UpdateServiceRequest;
import com.example.ead_backend.dto.service.CategoryResponse;
import com.example.ead_backend.dto.service.ServiceResponse;
import com.example.ead_backend.exception.ResourceNotFoundException;
import com.example.ead_backend.service.servicemanagement.ServiceCategoryService;
import com.example.ead_backend.service.servicemanagement.ServiceManagementService;
import com.example.ead_backend.contoller.admin.AdminServiceController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for AdminServiceController
 * Author: Member 9 - Dilminda W.W.C.
 */
@WebMvcTest(AdminServiceController.class)
@Import(TestSecurityConfig.class)
class AdminServiceControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private ServiceManagementService serviceManagementService;
    
    @MockBean
    private ServiceCategoryService serviceCategoryService;
    
    private CreateServiceRequest createServiceRequest;
    private UpdateServiceRequest updateServiceRequest;
    private ServiceResponse serviceResponse;
    private CategoryRequest categoryRequest;
    private CategoryResponse categoryResponse;
    
    @BeforeEach
    void setUp() {
        // Setup test data
        createServiceRequest = CreateServiceRequest.builder()
                .categoryId(1L)
                .name("Oil Change")
                .description("Complete oil and filter change service")
                .estimatedHours(new BigDecimal("0.5"))
                .price(new BigDecimal("3500.00"))
                .isActive(true)
                .build();
        
        updateServiceRequest = UpdateServiceRequest.builder()
                .name("Premium Oil Change")
                .price(new BigDecimal("4500.00"))
                .build();
        
        serviceResponse = new ServiceResponse();
        serviceResponse.setId(1L);
        serviceResponse.setCategoryId(1L);
        serviceResponse.setCategoryName("Maintenance");
        serviceResponse.setServiceName("Oil Change");
        serviceResponse.setDescription("Complete oil and filter change service");
        serviceResponse.setEstimatedDuration(30); // 0.5 hours * 60 minutes
        serviceResponse.setPrice(new BigDecimal("3500.00"));
        serviceResponse.setIsActive(true);
        
        categoryRequest = CategoryRequest.builder()
                .name("Maintenance")
                .description("Regular maintenance services")
                .isActive(true)
                .build();
        
        categoryResponse = CategoryResponse.builder()
                .id(1L)
                .name("Maintenance")
                .description("Regular maintenance services")
                .isActive(true)
                .serviceCount(5)
                .createdAt(LocalDateTime.now())
                .build();
    }
    
    // ==================== SERVICE TESTS ====================
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void createService_WithValidData_ShouldReturnCreated() throws Exception {
        // Arrange
        when(serviceManagementService.createService(any(CreateServiceRequest.class)))
                .thenReturn(serviceResponse);
        
        // Act & Assert
        mockMvc.perform(post("/api/admin/services")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createServiceRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Service created successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.serviceName").value("Oil Change"))
                .andExpect(jsonPath("$.data.price").value(3500.00));
        
        verify(serviceManagementService, times(1)).createService(any(CreateServiceRequest.class));
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void createService_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Arrange - Invalid request (missing required fields)
        CreateServiceRequest invalidRequest = CreateServiceRequest.builder()
                .name("") // Empty name
                .build();
        
        // Act & Assert
        mockMvc.perform(post("/api/admin/services")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
        
        verify(serviceManagementService, never()).createService(any());
    }
    
    @Test
    @WithMockUser(roles = "CUSTOMER")
    void createService_WithCustomerRole_ShouldReturnForbidden() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/admin/services")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createServiceRequest)))
                .andDo(print())
                .andExpect(status().isForbidden());
        
        verify(serviceManagementService, never()).createService(any());
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllServices_ShouldReturnServiceList() throws Exception {
        // Arrange
        List<ServiceResponse> services = Arrays.asList(serviceResponse);
        when(serviceManagementService.getAllServices()).thenReturn(services);
        
        // Act & Assert
        mockMvc.perform(get("/api/admin/services")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].serviceName").value("Oil Change"));
        
        verify(serviceManagementService, times(1)).getAllServices();
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void getServiceById_WithValidId_ShouldReturnService() throws Exception {
        // Arrange
        when(serviceManagementService.getServiceById(1L)).thenReturn(serviceResponse);
        
        // Act & Assert
        mockMvc.perform(get("/api/admin/services/1")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.serviceName").value("Oil Change"));
        
        verify(serviceManagementService, times(1)).getServiceById(1L);
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void getServiceById_WithInvalidId_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(serviceManagementService.getServiceById(999L))
                .thenThrow(new ResourceNotFoundException("Service not found"));
        
        // Act & Assert
        mockMvc.perform(get("/api/admin/services/999")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isNotFound());
        
        verify(serviceManagementService, times(1)).getServiceById(999L);
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateService_WithValidData_ShouldReturnUpdatedService() throws Exception {
        // Arrange
        ServiceResponse updatedResponse = new ServiceResponse();
        updatedResponse.setId(1L);
        updatedResponse.setServiceName("Premium Oil Change");
        updatedResponse.setPrice(new BigDecimal("4500.00"));
        
        when(serviceManagementService.updateService(eq(1L), any(UpdateServiceRequest.class)))
                .thenReturn(updatedResponse);
        
        // Act & Assert
        mockMvc.perform(put("/api/admin/services/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateServiceRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.serviceName").value("Premium Oil Change"))
                .andExpect(jsonPath("$.data.price").value(4500.00));
        
        verify(serviceManagementService, times(1)).updateService(eq(1L), any(UpdateServiceRequest.class));
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteService_WithValidId_ShouldReturnSuccess() throws Exception {
        // Arrange
        doNothing().when(serviceManagementService).deleteService(1L);
        
        // Act & Assert
        mockMvc.perform(delete("/api/admin/services/1")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Service deleted successfully"));
        
        verify(serviceManagementService, times(1)).deleteService(1L);
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void toggleServiceStatus_ShouldReturnUpdatedService() throws Exception {
        // Arrange
        ServiceResponse toggledResponse = new ServiceResponse();
        toggledResponse.setId(1L);
        toggledResponse.setServiceName("Oil Change");
        toggledResponse.setIsActive(false);
        
        when(serviceManagementService.toggleServiceStatus(1L)).thenReturn(toggledResponse);
        
        // Act & Assert
        mockMvc.perform(patch("/api/admin/services/1/toggle-status")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.isActive").value(false));
        
        verify(serviceManagementService, times(1)).toggleServiceStatus(1L);
    }
    
    // ==================== CATEGORY TESTS ====================
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void createCategory_WithValidData_ShouldReturnCreated() throws Exception {
        // Arrange
        when(serviceCategoryService.createCategory(any(CategoryRequest.class)))
                .thenReturn(categoryResponse);
        
        // Act & Assert
        mockMvc.perform(post("/api/admin/services/categories")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Maintenance"))
                .andExpect(jsonPath("$.data.serviceCount").value(5));
        
        verify(serviceCategoryService, times(1)).createCategory(any(CategoryRequest.class));
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllCategories_ShouldReturnCategoryList() throws Exception {
        // Arrange
        List<CategoryResponse> categories = Arrays.asList(categoryResponse);
        when(serviceCategoryService.getAllCategories()).thenReturn(categories);
        
        // Act & Assert
        mockMvc.perform(get("/api/admin/services/categories")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].name").value("Maintenance"));
        
        verify(serviceCategoryService, times(1)).getAllCategories();
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void getCategoryById_WithValidId_ShouldReturnCategory() throws Exception {
        // Arrange
        when(serviceCategoryService.getCategoryById(1L)).thenReturn(categoryResponse);
        
        // Act & Assert
        mockMvc.perform(get("/api/admin/services/categories/1")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Maintenance"));
        
        verify(serviceCategoryService, times(1)).getCategoryById(1L);
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateCategory_WithValidData_ShouldReturnUpdatedCategory() throws Exception {
        // Arrange
        CategoryResponse updatedResponse = CategoryResponse.builder()
                .id(1L)
                .name("Premium Maintenance")
                .build();
        
        when(serviceCategoryService.updateCategory(eq(1L), any(CategoryRequest.class)))
                .thenReturn(updatedResponse);
        
        // Act & Assert
        mockMvc.perform(put("/api/admin/services/categories/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Premium Maintenance"));
        
        verify(serviceCategoryService, times(1)).updateCategory(eq(1L), any(CategoryRequest.class));
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteCategory_WithValidId_ShouldReturnSuccess() throws Exception {
        // Arrange
        doNothing().when(serviceCategoryService).deleteCategory(1L);
        
        // Act & Assert
        mockMvc.perform(delete("/api/admin/services/categories/1")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Category deleted successfully"));
        
        verify(serviceCategoryService, times(1)).deleteCategory(1L);
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void searchCategories_WithKeyword_ShouldReturnFilteredCategories() throws Exception {
        // Arrange
        List<CategoryResponse> categories = Arrays.asList(categoryResponse);
        when(serviceCategoryService.searchCategories("Maintenance")).thenReturn(categories);
        
        // Act & Assert
        mockMvc.perform(get("/api/admin/services/categories")
                .with(csrf())
                .param("search", "Maintenance"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data", hasSize(1)));
        
        verify(serviceCategoryService, times(1)).searchCategories("Maintenance");
    }
}