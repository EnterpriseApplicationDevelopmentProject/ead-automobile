package com.example.ead_backend.service.impl;

import com.example.ead_backend.dto.AppointmentDTO;
import com.example.ead_backend.dto.EmployeeDTO;
import com.example.ead_backend.dto.ProjectDTO;
import com.example.ead_backend.dto.TaskDTO;
import com.example.ead_backend.model.entity.Appointment;
import com.example.ead_backend.model.entity.Employee;
import com.example.ead_backend.model.entity.Project;
import com.example.ead_backend.model.entity.Task;
import com.example.ead_backend.model.enums.AppointmentStatus;
import com.example.ead_backend.model.enums.ProjectStatus;
import com.example.ead_backend.repository.AppointmentRepository;
import com.example.ead_backend.repository.EmployeeRepository;
import com.example.ead_backend.repository.ProjectRepository;
import com.example.ead_backend.repository.TaskRepository;
import com.example.ead_backend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private ProjectRepository projectRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private TaskRepository taskRepository;
    
    // ==================== Appointment Management ====================
    
    @Override
    public List<AppointmentDTO> getAllPendingAppointments() {
        List<Appointment> appointments = appointmentRepository.findAllPendingAppointments();
        return appointments.stream()
                .map(this::convertAppointmentToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<AppointmentDTO> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments.stream()
                .map(this::convertAppointmentToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public AppointmentDTO getAppointmentById(String appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));
        return convertAppointmentToDTO(appointment);
    }
    
    @Override
    @Transactional
    public AppointmentDTO assignAppointmentToEmployee(String appointmentId, String employeeId) {
        // Fetch appointment
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));
        
        // Fetch employee
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));
        
        // Check if employee is available
        if (!employee.isAvailable()) {
            throw new RuntimeException("Employee is not available for assignment");
        }
        
        // Assign appointment to employee
        appointment.setAssignedEmployeeId(employeeId);
        appointment.setStatus(AppointmentStatus.ASSIGNED);
        appointment.setUpdatedAt(LocalDateTime.now());
        
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return convertAppointmentToDTO(savedAppointment);
    }
    
    // ==================== Project Management ====================
    
    @Override
    public List<ProjectDTO> getAllPendingProjects() {
        List<Project> projects = projectRepository.findAllPendingProjects();
        return projects.stream()
                .map(this::convertProjectToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ProjectDTO> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream()
                .map(this::convertProjectToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public ProjectDTO getProjectById(String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));
        return convertProjectToDTO(project);
    }
    
    @Override
    @Transactional
    public ProjectDTO assignProjectToEmployee(String projectId, String employeeId) {
        // Fetch project
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));
        
        // Fetch employee
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));
        
        // Check if employee is available
        if (!employee.isAvailable()) {
            throw new RuntimeException("Employee is not available for assignment");
        }
        
        // Assign project to employee
        project.setAssignedEmployeeId(employeeId);
        project.setStatus(ProjectStatus.ASSIGNED);
        project.setUpdatedAt(LocalDateTime.now());
        
        Project savedProject = projectRepository.save(project);
        return convertProjectToDTO(savedProject);
    }
    
    // ==================== Employee Management ====================
    
    @Override
    public List<EmployeeDTO> getAllAvailableEmployees() {
        List<Employee> employees = employeeRepository.findAllAvailableEmployees();
        return employees.stream()
                .map(this::convertEmployeeToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(this::convertEmployeeToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public EmployeeDTO getEmployeeById(String employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));
        return convertEmployeeToDTO(employee);
    }
    
    // ==================== NEW CRUD Operations ====================
    
    // Appointment CRUD
    @Override
    @Transactional
    public AppointmentDTO createAppointment(AppointmentDTO appointmentDTO) {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(UUID.randomUUID().toString());
        appointment.setCustomerId(appointmentDTO.getCustomerId());
        appointment.setVehicleId(appointmentDTO.getVehicleId());
        appointment.setDescription(appointmentDTO.getDescription());
        appointment.setAppointmentDateTime(appointmentDTO.getAppointmentDateTime());
        appointment.setStatus(appointmentDTO.getStatus() != null ? appointmentDTO.getStatus() : AppointmentStatus.PENDING);
        appointment.setAssignedEmployeeId(appointmentDTO.getAssignedEmployeeId());
        appointment.setCreatedAt(LocalDateTime.now());
        appointment.setUpdatedAt(LocalDateTime.now());
        
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return convertAppointmentToDTO(savedAppointment);
    }
    
    @Override
    @Transactional
    public AppointmentDTO updateAppointment(String appointmentId, AppointmentDTO appointmentDTO) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));
        
        if (appointmentDTO.getCustomerId() != null) appointment.setCustomerId(appointmentDTO.getCustomerId());
        if (appointmentDTO.getVehicleId() != null) appointment.setVehicleId(appointmentDTO.getVehicleId());
        if (appointmentDTO.getDescription() != null) appointment.setDescription(appointmentDTO.getDescription());
        if (appointmentDTO.getAppointmentDateTime() != null) appointment.setAppointmentDateTime(appointmentDTO.getAppointmentDateTime());
        if (appointmentDTO.getStatus() != null) appointment.setStatus(appointmentDTO.getStatus());
        if (appointmentDTO.getAssignedEmployeeId() != null) appointment.setAssignedEmployeeId(appointmentDTO.getAssignedEmployeeId());
        appointment.setUpdatedAt(LocalDateTime.now());
        
        Appointment updatedAppointment = appointmentRepository.save(appointment);
        return convertAppointmentToDTO(updatedAppointment);
    }
    
    @Override
    @Transactional
    public void deleteAppointment(String appointmentId) {
        if (!appointmentRepository.existsById(appointmentId)) {
            throw new RuntimeException("Appointment not found with id: " + appointmentId);
        }
        appointmentRepository.deleteById(appointmentId);
    }
    
    // Project CRUD
    @Override
    @Transactional
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        Project project = new Project();
        project.setProjectId(UUID.randomUUID().toString());
        project.setCustomerId(projectDTO.getCustomerId());
        project.setVehicleId(projectDTO.getVehicleId());
        project.setProjectName(projectDTO.getProjectName());
        project.setDescription(projectDTO.getDescription());
        project.setStartDate(projectDTO.getStartDate());
        project.setExpectedEndDate(projectDTO.getExpectedEndDate());
        project.setStatus(projectDTO.getStatus() != null ? projectDTO.getStatus() : ProjectStatus.PENDING);
        project.setAssignedEmployeeId(projectDTO.getAssignedEmployeeId());
        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());
        
        Project savedProject = projectRepository.save(project);
        return convertProjectToDTO(savedProject);
    }
    
    @Override
    @Transactional
    public ProjectDTO updateProject(String projectId, ProjectDTO projectDTO) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));
        
        if (projectDTO.getCustomerId() != null) project.setCustomerId(projectDTO.getCustomerId());
        if (projectDTO.getVehicleId() != null) project.setVehicleId(projectDTO.getVehicleId());
        if (projectDTO.getProjectName() != null) project.setProjectName(projectDTO.getProjectName());
        if (projectDTO.getDescription() != null) project.setDescription(projectDTO.getDescription());
        if (projectDTO.getStartDate() != null) project.setStartDate(projectDTO.getStartDate());
        if (projectDTO.getExpectedEndDate() != null) project.setExpectedEndDate(projectDTO.getExpectedEndDate());
        if (projectDTO.getStatus() != null) project.setStatus(projectDTO.getStatus());
        if (projectDTO.getAssignedEmployeeId() != null) project.setAssignedEmployeeId(projectDTO.getAssignedEmployeeId());
        project.setUpdatedAt(LocalDateTime.now());
        
        Project updatedProject = projectRepository.save(project);
        return convertProjectToDTO(updatedProject);
    }
    
    @Override
    @Transactional
    public void deleteProject(String projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new RuntimeException("Project not found with id: " + projectId);
        }
        projectRepository.deleteById(projectId);
    }
    
    // Task CRUD
    @Override
    public List<TaskDTO> getAllTasksForAppointment(String appointmentId) {
        List<Task> tasks = taskRepository.findByAppointmentId(appointmentId);
        return tasks.stream()
                .map(this::convertTaskToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public TaskDTO getTaskById(String taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
        return convertTaskToDTO(task);
    }
    
    @Override
    @Transactional
    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = new Task();
        task.setTaskId(UUID.randomUUID().toString());
        task.setAppointmentId(taskDTO.getAppointmentId());
        task.setTaskName(taskDTO.getTaskName());
        task.setDescription(taskDTO.getDescription());
        task.setAssignedEmployeeId(taskDTO.getAssignedEmployeeId());
        task.setStatus(taskDTO.getStatus());
        task.setDueDate(taskDTO.getDueDate());
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        
        Task savedTask = taskRepository.save(task);
        return convertTaskToDTO(savedTask);
    }
    
    @Override
    @Transactional
    public TaskDTO updateTask(String taskId, TaskDTO taskDTO) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
        
        if (taskDTO.getAppointmentId() != null) task.setAppointmentId(taskDTO.getAppointmentId());
        if (taskDTO.getTaskName() != null) task.setTaskName(taskDTO.getTaskName());
        if (taskDTO.getDescription() != null) task.setDescription(taskDTO.getDescription());
        if (taskDTO.getAssignedEmployeeId() != null) task.setAssignedEmployeeId(taskDTO.getAssignedEmployeeId());
        if (taskDTO.getStatus() != null) task.setStatus(taskDTO.getStatus());
        if (taskDTO.getDueDate() != null) task.setDueDate(taskDTO.getDueDate());
        task.setUpdatedAt(LocalDateTime.now());
        
        Task updatedTask = taskRepository.save(task);
        return convertTaskToDTO(updatedTask);
    }
    
    @Override
    @Transactional
    public void deleteTask(String taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new RuntimeException("Task not found with id: " + taskId);
        }
        taskRepository.deleteById(taskId);
    }
    
    // Employee CRUD
    @Override
    @Transactional
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setEmployeeId(UUID.randomUUID().toString());
        employee.setName(employeeDTO.getName());
        employee.setEmail(employeeDTO.getEmail());
        employee.setPhone(employeeDTO.getPhone());
        employee.setSpecialization(employeeDTO.getSpecialization());
        employee.setAvailable(employeeDTO.isAvailable());
        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());
        
        Employee savedEmployee = employeeRepository.save(employee);
        return convertEmployeeToDTO(savedEmployee);
    }
    
    @Override
    @Transactional
    public EmployeeDTO updateEmployee(String employeeId, EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));
        
        if (employeeDTO.getName() != null) employee.setName(employeeDTO.getName());
        if (employeeDTO.getEmail() != null) employee.setEmail(employeeDTO.getEmail());
        if (employeeDTO.getPhone() != null) employee.setPhone(employeeDTO.getPhone());
        if (employeeDTO.getSpecialization() != null) employee.setSpecialization(employeeDTO.getSpecialization());
        employee.setAvailable(employeeDTO.isAvailable());
        employee.setUpdatedAt(LocalDateTime.now());
        
        Employee updatedEmployee = employeeRepository.save(employee);
        return convertEmployeeToDTO(updatedEmployee);
    }
    
    @Override
    @Transactional
    public void deleteEmployee(String employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new RuntimeException("Employee not found with id: " + employeeId);
        }
        employeeRepository.deleteById(employeeId);
    }
    
    // ==================== Helper Methods (DTO Converters) ====================
    
    private AppointmentDTO convertAppointmentToDTO(Appointment appointment) {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setAppointmentId(appointment.getAppointmentId());
        dto.setCustomerId(appointment.getCustomerId());
        dto.setVehicleId(appointment.getVehicleId());
        dto.setDescription(appointment.getDescription());
        dto.setAppointmentDateTime(appointment.getAppointmentDateTime());
        dto.setStatus(appointment.getStatus());
        dto.setAssignedEmployeeId(appointment.getAssignedEmployeeId());
        
        // Set assigned employee name if available
        if (appointment.getAssignedEmployeeId() != null) {
            employeeRepository.findById(appointment.getAssignedEmployeeId())
                    .ifPresent(emp -> dto.setAssignedEmployeeName(emp.getName()));
        }
        
        dto.setCreatedAt(appointment.getCreatedAt());
        dto.setUpdatedAt(appointment.getUpdatedAt());
        return dto;
    }
    
    private ProjectDTO convertProjectToDTO(Project project) {
        ProjectDTO dto = new ProjectDTO();
        dto.setProjectId(project.getProjectId());
        dto.setCustomerId(project.getCustomerId());
        dto.setVehicleId(project.getVehicleId());
        dto.setProjectName(project.getProjectName());
        dto.setDescription(project.getDescription());
        dto.setStartDate(project.getStartDate());
        dto.setExpectedEndDate(project.getExpectedEndDate());
        dto.setStatus(project.getStatus());
        dto.setAssignedEmployeeId(project.getAssignedEmployeeId());
        
        // Set assigned employee name if available
        if (project.getAssignedEmployeeId() != null) {
            employeeRepository.findById(project.getAssignedEmployeeId())
                    .ifPresent(emp -> dto.setAssignedEmployeeName(emp.getName()));
        }
        
        dto.setCreatedAt(project.getCreatedAt());
        dto.setUpdatedAt(project.getUpdatedAt());
        return dto;
    }
    
    private EmployeeDTO convertEmployeeToDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmployeeId(employee.getEmployeeId());
        dto.setName(employee.getName());
        dto.setEmail(employee.getEmail());
        dto.setPhone(employee.getPhone());
        dto.setSpecialization(employee.getSpecialization());
        dto.setAvailable(employee.isAvailable());
        dto.setCreatedAt(employee.getCreatedAt());
        dto.setUpdatedAt(employee.getUpdatedAt());
        return dto;
    }
    
    private TaskDTO convertTaskToDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setTaskId(task.getTaskId());
        dto.setAppointmentId(task.getAppointmentId());
        dto.setTaskName(task.getTaskName());
        dto.setDescription(task.getDescription());
        dto.setAssignedEmployeeId(task.getAssignedEmployeeId());
        
        // Set assigned employee name if available
        if (task.getAssignedEmployeeId() != null) {
            employeeRepository.findById(task.getAssignedEmployeeId())
                    .ifPresent(emp -> dto.setAssignedEmployeeName(emp.getName()));
        }
        
        dto.setStatus(task.getStatus());
        dto.setDueDate(task.getDueDate());
        dto.setCreatedAt(task.getCreatedAt());
        dto.setUpdatedAt(task.getUpdatedAt());
        return dto;
    }
}
