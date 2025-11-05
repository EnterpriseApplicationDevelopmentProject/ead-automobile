package com.example.ead_backend.service;

import com.example.ead_backend.dto.EmployeeCreateDTO;
import com.example.ead_backend.model.entity.Employee;
import com.example.ead_backend.model.entity.User;
import com.example.ead_backend.model.enums.Role;

import java.time.LocalDate;

public interface EmployeeService {
    Employee createEmployee(User user, Role role, LocalDate joinedDate);
    Employee findByUserId(Long userId);
    
    EmployeeCreateDTO getEmployeeById(Long id);
    EmployeeCreateDTO updateEmployee(Long id, EmployeeCreateDTO dto);
}
