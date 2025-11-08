package com.example.ead_backend.service.impl;

import com.example.ead_backend.dto.*;
import com.example.ead_backend.model.entity.Customer;
import com.example.ead_backend.model.entity.Employee;
import com.example.ead_backend.model.entity.User;
import com.example.ead_backend.model.enums.Role;
import com.example.ead_backend.repository.CustomerRepository;
import com.example.ead_backend.repository.EmployeeRepository;
import com.example.ead_backend.service.AppointmentService;
import com.example.ead_backend.service.EmployeeService;
import com.example.ead_backend.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private AdminServiceImpl adminService;

    private User user;
    private Employee employee;
    private Customer customer;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");

        employee = new Employee();
        employee.setId(1L);
        employee.setUser(user);
        employee.setRole(Role.EMPLOYEE);
        employee.setJoinedDate(LocalDate.now());

        customer = new Customer();
        customer.setId(1L);
        customer.setUser(user);
        customer.setPhoneNumber("1234567890");
    }

    @Test
    void testCreateEmployee_Success() {
        // Given
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john.doe@example.com");
        request.setPassword("password123");

        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userService.createUser(anyString(), anyString(), anyString(), anyString())).thenReturn(user);
        when(employeeService.createEmployee(any(User.class), eq(Role.EMPLOYEE), any(LocalDate.class)))
                .thenReturn(employee);

        // When
        EmployeeCreateDTO result = adminService.createEmployee(request);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("john.doe@example.com", result.getEmail());
        verify(passwordEncoder).encode("password123");
        verify(userService).createUser("John", "Doe", "encodedPassword", "john.doe@example.com");
        verify(employeeService).createEmployee(any(User.class), eq(Role.EMPLOYEE), any(LocalDate.class));
    }

    @Test
    void testGetAllAppointments() {
        // Given
        List<AppointmentDTO> appointments = Arrays.asList(new AppointmentDTO());
        when(appointmentService.getAllAppointments()).thenReturn(appointments);

        // When
        List<AppointmentDTO> result = adminService.getAllAppointments();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(appointmentService).getAllAppointments();
    }

    @Test
    void testGetAllCustomers() {
        // Given
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findAll()).thenReturn(customers);

        // When
        List<CustomerDTO> result = adminService.getAllCustomers();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Doe", result.get(0).getLastName());
        assertEquals("john.doe@example.com", result.get(0).getEmail());
        verify(customerRepository).findAll();
    }

    @Test
    void testGetAllEmployees() {
        // Given
        List<Employee> employees = Arrays.asList(employee);
        when(employeeRepository.findAll()).thenReturn(employees);

        // When
        List<EmployeeDTO> result = adminService.getAllEmployees();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Doe", result.get(0).getLastName());
        assertEquals("john.doe@example.com", result.get(0).getEmail());
        verify(employeeRepository).findAll();
    }

    @Test
    void testGetAllProjects() {
        // Given
        List<ProjectDTO> projects = Arrays.asList(new ProjectDTO());
        when(projectService.getAllProjects()).thenReturn(projects);

        // When
        List<ProjectDTO> result = adminService.getAllProjects();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(projectService).getAllProjects();
    }
}
