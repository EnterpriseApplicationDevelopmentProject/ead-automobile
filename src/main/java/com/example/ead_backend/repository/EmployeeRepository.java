package com.example.ead_backend.repository;

import com.example.ead_backend.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    Optional<Employee> findByEmail(String email);
    List<Employee> findByIsAvailable(boolean isAvailable);
}
