package com.example.ead_backend.service.impl;

import com.example.ead_backend.dto.AppointmentDTO;
import com.example.ead_backend.dto.CustomerDTO;
import com.example.ead_backend.dto.DashboardStatsDTO;
import com.example.ead_backend.dto.ProjectDTO;
import com.example.ead_backend.model.entity.Appointment;
import com.example.ead_backend.model.entity.Customer;
import com.example.ead_backend.model.entity.Project;
import com.example.ead_backend.model.enums.AppointmentStatus;
import com.example.ead_backend.model.enums.ProjectStatus;
import com.example.ead_backend.repository.AppointmentRepository;
import com.example.ead_backend.repository.CustomerRepository;
import com.example.ead_backend.repository.ProjectRepository;
import com.example.ead_backend.repository.VehicleRepository;
import com.example.ead_backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;
    private final AppointmentRepository appointmentRepository;
    private final ProjectRepository projectRepository;

    @Override
    public DashboardStatsDTO getDashboardStats(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));

        // Count vehicles for this customer
        int totalVehicles = vehicleRepository.findByOwner_Id(customerId).size();

        // Count upcoming appointments (PENDING, CONFIRMED, IN_PROGRESS)
        List<Appointment> allAppointments = customer.getAppointments();
        int upcomingAppointments = (int) allAppointments.stream()
                .filter(apt -> apt.getStatus() == AppointmentStatus.PENDING ||
                               apt.getStatus() == AppointmentStatus.CONFIRMED ||
                               apt.getStatus() == AppointmentStatus.IN_PROGRESS)
                .filter(apt -> apt.getAppointmentTime().isAfter(LocalDateTime.now()) ||
                               apt.getStatus() == AppointmentStatus.IN_PROGRESS)
                .count();

        // Count ongoing projects (IN_PROGRESS, ASSIGNED)
        List<Project> allProjects = customer.getProjects();
        int ongoingProjects = (int) allProjects.stream()
                .filter(proj -> proj.getStatus() == ProjectStatus.IN_PROGRESS ||
                                proj.getStatus() == ProjectStatus.ASSIGNED ||
                                proj.getStatus() == ProjectStatus.UNDER_REVIEW)
                .count();

        return DashboardStatsDTO.builder()
                .totalVehicles(totalVehicles)
                .upcomingAppointments(upcomingAppointments)
                .ongoingProjects(ongoingProjects)
                .build();
    }

    @Override
    public CustomerDTO getCustomerProfile(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));

        return CustomerDTO.builder()
                .id(customer.getId())
                .name(customer.getUser().getFirstName() + " " + customer.getUser().getLastName())
                .email(customer.getUser().getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .address(customer.getAddress())
                .createdAt(customer.getCreatedAt())
                .build();
    }

    @Override
    public List<AppointmentDTO> getUpcomingAppointments(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
        
        List<Appointment> appointments = customer.getAppointments();
        
        return appointments.stream()
                .filter(apt -> (apt.getStatus() == AppointmentStatus.PENDING ||
                                apt.getStatus() == AppointmentStatus.CONFIRMED ||
                                apt.getStatus() == AppointmentStatus.IN_PROGRESS) &&
                               (apt.getAppointmentTime().isAfter(LocalDateTime.now()) ||
                                apt.getStatus() == AppointmentStatus.IN_PROGRESS))
                .sorted((a1, a2) -> a1.getAppointmentTime().compareTo(a2.getAppointmentTime()))
                .limit(10)
                .map(this::convertToAppointmentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> getAllAppointments(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
        
        List<Appointment> appointments = customer.getAppointments();
        
        return appointments.stream()
                .sorted((a1, a2) -> a2.getAppointmentTime().compareTo(a1.getAppointmentTime()))
                .map(this::convertToAppointmentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectDTO> getOngoingProjects(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
        
        List<Project> projects = customer.getProjects();
        
        return projects.stream()
                .filter(proj -> proj.getStatus() == ProjectStatus.IN_PROGRESS ||
                                proj.getStatus() == ProjectStatus.ASSIGNED ||
                                proj.getStatus() == ProjectStatus.UNDER_REVIEW)
                .sorted((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()))
                .limit(10)
                .map(this::convertToProjectDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectDTO> getAllProjects(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
        
        List<Project> projects = customer.getProjects();
        
        return projects.stream()
                .sorted((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()))
                .map(this::convertToProjectDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectDTO> getCompletedProjects(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
        
        List<Project> projects = customer.getProjects();
        
        return projects.stream()
                .filter(proj -> proj.getStatus() == ProjectStatus.COMPLETED)
                .sorted((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()))
                .map(this::convertToProjectDTO)
                .collect(Collectors.toList());
    }

    private AppointmentDTO convertToAppointmentDTO(Appointment appointment) {
        String serviceName = appointment.getTasks() != null && !appointment.getTasks().isEmpty()
                ? String.join(", ", appointment.getTasks())
                : "Service";

        return AppointmentDTO.builder()
                .id(appointment.getAppointmentId())
                .customerId(appointment.getCustomer().getId())
                .customerName(appointment.getCustomer().getUser().getFirstName() + " " + appointment.getCustomer().getUser().getLastName())
                .vehicleId(Long.parseLong(appointment.getVehicle().getId()))
                .vehicleNumber(appointment.getVehicle().getLicensePlate())
                .vehicleModel(appointment.getVehicle().getModel())
                .employeeId(appointment.getEmployee() != null ? appointment.getEmployee().getId() : null)
                .employeeName(appointment.getEmployee() != null ? (appointment.getEmployee().getUser().getFirstName() + " " + appointment.getEmployee().getUser().getLastName()) : null)
                .appointmentTime(appointment.getAppointmentTime())
                .status(appointment.getStatus().toString())
                .tasks(appointment.getTasks())
                .serviceName(serviceName)
                .customerNotes(appointment.getCustomerNotes())
                .estimatedDurationMinutes(appointment.getEstimatedDurationMinutes())
                .createdAt(appointment.getCreatedAt())
                .build();
    }

    private ProjectDTO convertToProjectDTO(Project project) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String time = project.getCreatedAt().format(timeFormatter);

        return ProjectDTO.builder()
                .id(project.getProjectId())
                .customerId(project.getCustomer().getId())
                .customerName(project.getCustomer().getUser().getFirstName() + " " + project.getCustomer().getUser().getLastName())
                .vehicleId(Long.parseLong(project.getVehicle().getId()))
                .vehicleNumber(project.getVehicle().getLicensePlate())
                .vehicleModel(project.getVehicle().getModel())
                .vehicleType(project.getVehicle().getMake() + " " + project.getVehicle().getModel())
                .employeeId(project.getEmployee() != null ? project.getEmployee().getId() : null)
                .employeeName(project.getEmployee() != null ? (project.getEmployee().getUser().getFirstName() + " " + project.getEmployee().getUser().getLastName()) : null)
                .assignedEmployee(project.getEmployee() != null ? (project.getEmployee().getUser().getFirstName() + " " + project.getEmployee().getUser().getLastName()) : "Unassigned")
                .serviceDescription(project.getServiceDescription())
                .taskName(project.getServiceDescription().length() > 50 
                        ? project.getServiceDescription().substring(0, 50) + "..." 
                        : project.getServiceDescription())
                .description(project.getServiceDescription())
                .status(project.getStatus().toString())
                .adminNotes(project.getAdminNotes())
                .employeeNotes(project.getEmployeeNotes())
                .estimatedCost(project.getEstimatedCost())
                .estimatedDurationDays(project.getEstimatedDurationDays())
                .startDate(project.getCreatedAt())
                .time(time)
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .assignedAt(project.getAssignedAt())
                .completedAt(project.getCompletedAt())
                .build();
    }
}
