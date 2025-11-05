package com.example.ead_backend.service;

import java.util.List;

import com.example.ead_backend.dto.VehicleDTO;

public interface VehicleService {
    VehicleDTO createVehicle(VehicleDTO vehicleDTO);

    VehicleDTO getVehicleById(String id);

    List<VehicleDTO> getAllVehicles();

    VehicleDTO updateVehicle(String id, VehicleDTO vehicleDTO);

    void deleteVehicle(String id);
}
