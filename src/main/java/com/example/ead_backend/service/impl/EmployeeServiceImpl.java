package com.example.ead_backend.service.impl;

import com.example.ead_backend.dto.EmployeeCreateDTO;
import com.example.ead_backend.mapper.EmployeeMapper;
import com.example.ead_backend.model.entity.Employee;
import com.example.ead_backend.model.entity.User;
import com.example.ead_backend.model.enums.Role;
import com.example.ead_backend.repository.EmployeeRepository;
import com.example.ead_backend.service.EmployeeService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    @Transactional
    public Employee createEmployee(User user, Role role, LocalDate joinedDate) {
        if (role != Role.ADMIN && role != Role.EMPLOYEE) {
            throw new IllegalArgumentException("Invalid role for employee");
        }
        Employee employee = new Employee(user, role, joinedDate);
        return employeeRepository.save(employee);
    }

    @Override
    public Employee findByUserId(Long userId) {
        return employeeRepository.findByUserId(userId).orElse(null);
    }

    @Override
    public EmployeeCreateDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)  
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        return employeeMapper.toDTO(employee);  
    }

    @Override
    public EmployeeCreateDTO updateEmployee(Long id, EmployeeCreateDTO dto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        
        employeeMapper.updateEntityFromDTO(dto, employee);
        
        return employeeMapper.toDTO(employee);  
    }
}
