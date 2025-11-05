package com.example.ead_backend.repository;

import com.example.ead_backend.model.entity.Customer;
import com.example.ead_backend.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    Optional<Customer> findByUser(User user);
    Optional<Customer> findByUserId(Long userId);
    Optional<Customer> findByUserEmail(String email);
    boolean existsByUserEmail(String email);
}
