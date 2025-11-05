package com.example.ead_backend.service;

import com.example.ead_backend.dto.AppointmentDTO;
import com.example.ead_backend.dto.CustomerDTO;
import com.example.ead_backend.dto.DashboardStatsDTO;
import com.example.ead_backend.dto.ProjectDTO;

import java.util.List;

public interface DashboardService {
    DashboardStatsDTO getDashboardStats(Long customerId);
    CustomerDTO getCustomerProfile(Long customerId);
    List<AppointmentDTO> getUpcomingAppointments(Long customerId);
    List<AppointmentDTO> getAllAppointments(Long customerId);
    List<ProjectDTO> getOngoingProjects(Long customerId);
    List<ProjectDTO> getAllProjects(Long customerId);
    List<ProjectDTO> getCompletedProjects(Long customerId);
}
