package com.example.ead_backend.service.impl;

import com.example.ead_backend.dto.CustomerProfileDTO;
import com.example.ead_backend.dto.UpdateCustomerProfileRequest;
import com.example.ead_backend.model.entity.Customer;
import com.example.ead_backend.model.entity.User;
import com.example.ead_backend.repository.CustomerRepository;
import com.example.ead_backend.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerProfileServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private UserRepo userRepository;

    @InjectMocks
    private CustomerProfileServiceImpl customerProfileService;

    private User user;
    private Customer customer;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");

        customer = new Customer();
        customer.setId(1L);
        customer.setUser(user);
        customer.setPhoneNumber("1234567890");
    }

    @Test
    void testGetCustomerProfileByUserId_Success() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(customerRepository.findByUserId(1L)).thenReturn(Optional.of(customer));

        // When
        CustomerProfileDTO result = customerProfileService.getCustomerProfileByUserId(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getUserId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("1234567890", result.getPhoneNumber());
        verify(userRepository).findById(1L);
        verify(customerRepository).findByUserId(1L);
    }

    @Test
    void testGetCustomerProfileByUserId_UserNotFound() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> customerProfileService.getCustomerProfileByUserId(999L));
        assertTrue(exception.getMessage().contains("User not found"));
        verify(customerRepository, never()).findByUserId(anyLong());
    }

    @Test
    void testGetCustomerProfileByUserId_CustomerNotFound() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(customerRepository.findByUserId(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> customerProfileService.getCustomerProfileByUserId(1L));
        assertTrue(exception.getMessage().contains("Customer not found"));
    }

    @Test
    void testGetCustomerProfileByCustomerId_Success() {
        // Given
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        // When
        CustomerProfileDTO result = customerProfileService.getCustomerProfileByCustomerId(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("1234567890", result.getPhoneNumber());
        verify(customerRepository).findById(1L);
    }

    @Test
    void testGetCustomerProfileByCustomerId_NotFound() {
        // Given
        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class,
                () -> customerProfileService.getCustomerProfileByCustomerId(999L));
    }

    @Test
    void testGetCustomerProfileByEmail_Success() {
        // Given
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));
        when(customerRepository.findByUserId(1L)).thenReturn(Optional.of(customer));

        // When
        CustomerProfileDTO result = customerProfileService.getCustomerProfileByEmail("john.doe@example.com");

        // Then
        assertNotNull(result);
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(userRepository).findByEmail("john.doe@example.com");
        verify(customerRepository).findByUserId(1L);
    }

    @Test
    void testGetCustomerProfileByEmail_UserNotFound() {
        // Given
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> customerProfileService.getCustomerProfileByEmail("nonexistent@example.com"));
        assertTrue(exception.getMessage().contains("User not found"));
    }

    @Test
    void testGetCustomerProfileByEmail_CustomerNotFound() {
        // Given
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));
        when(customerRepository.findByUserId(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> customerProfileService.getCustomerProfileByEmail("john.doe@example.com"));
        assertTrue(exception.getMessage().contains("Customer not found"));
    }

    @Test
    void testUpdateCustomerProfile_Success() {
        // Given
        UpdateCustomerProfileRequest request = new UpdateCustomerProfileRequest();
        request.setFirstName("Jane");
        request.setLastName("Smith");
        request.setPhoneNumber("9876543210");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(customerRepository.findByUserId(1L)).thenReturn(Optional.of(customer));
        when(userRepository.save(user)).thenReturn(user);
        when(customerRepository.save(customer)).thenReturn(customer);

        // When
        CustomerProfileDTO result = customerProfileService.updateCustomerProfile(1L, request);

        // Then
        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals("9876543210", result.getPhoneNumber());
        verify(userRepository).save(user);
        verify(customerRepository).save(customer);
    }

    @Test
    void testUpdateCustomerProfile_UserNotFound() {
        // Given
        UpdateCustomerProfileRequest request = new UpdateCustomerProfileRequest();
        request.setFirstName("Jane");
        request.setLastName("Smith");
        request.setPhoneNumber("9876543210");

        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> customerProfileService.updateCustomerProfile(999L, request));
        assertTrue(exception.getMessage().contains("User not found"));
        verify(userRepository, never()).save(any());
        verify(customerRepository, never()).save(any());
    }

    @Test
    void testUpdateCustomerProfile_CustomerNotFound() {
        // Given
        UpdateCustomerProfileRequest request = new UpdateCustomerProfileRequest();
        request.setFirstName("Jane");
        request.setLastName("Smith");
        request.setPhoneNumber("9876543210");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(customerRepository.findByUserId(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> customerProfileService.updateCustomerProfile(1L, request));
        assertTrue(exception.getMessage().contains("Customer not found"));
        verify(userRepository, never()).save(any());
        verify(customerRepository, never()).save(any());
    }
}
