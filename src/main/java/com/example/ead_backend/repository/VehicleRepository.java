package com.example.ead_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ead_backend.model.entity.Vehicle;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
    List<Vehicle> findByOwner_CustomerId(String customerId);
    Optional<Vehicle> findByLicensePlate(String licensePlate);
}
