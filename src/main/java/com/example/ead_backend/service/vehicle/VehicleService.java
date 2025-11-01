package com.example.ead_backend.service.vehicle;

import java.util.List;

import com.example.ead_backend.dto.vehicle.VehicleDTO;

public interface VehicleService {
    VehicleDTO createVehicle(VehicleDTO vehicleDTO);

    VehicleDTO getVehicleById(Long id);

    List<VehicleDTO> getAllVehicles();

    VehicleDTO updateVehicle(Long id, VehicleDTO vehicleDTO);

    void deleteVehicle(Long id);
}
