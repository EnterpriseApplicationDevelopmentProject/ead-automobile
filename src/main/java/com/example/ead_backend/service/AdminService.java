package com.example.ead_backend.service;

import com.example.ead_backend.dto.AppointmentDTO;
import com.example.ead_backend.dto.EmployeeDTO;
import com.example.ead_backend.dto.ProjectDTO;
import com.example.ead_backend.dto.TaskDTO;

import java.util.List;

public interface AdminService {
    
    // ==================== Appointment Management ====================
    List<AppointmentDTO> getAllPendingAppointments();
    List<AppointmentDTO> getAllAppointments();
    AppointmentDTO getAppointmentById(String appointmentId);
    AppointmentDTO assignAppointmentToEmployee(String appointmentId, String employeeId);
    AppointmentDTO createAppointment(AppointmentDTO appointmentDTO);
    AppointmentDTO updateAppointment(String appointmentId, AppointmentDTO appointmentDTO);
    void deleteAppointment(String appointmentId);
    
    // ==================== Project Management ====================
    List<ProjectDTO> getAllPendingProjects();
    List<ProjectDTO> getAllProjects();
    ProjectDTO getProjectById(String projectId);
    ProjectDTO assignProjectToEmployee(String projectId, String employeeId);
    ProjectDTO createProject(ProjectDTO projectDTO);
    ProjectDTO updateProject(String projectId, ProjectDTO projectDTO);
    void deleteProject(String projectId);
    
    // ==================== Task Management ====================
    List<TaskDTO> getAllTasksForAppointment(String appointmentId);
    TaskDTO getTaskById(String taskId);
    TaskDTO createTask(TaskDTO taskDTO);
    TaskDTO updateTask(String taskId, TaskDTO taskDTO);
    void deleteTask(String taskId);
    
    // ==================== Employee Management ====================
    List<EmployeeDTO> getAllAvailableEmployees();
    List<EmployeeDTO> getAllEmployees();
    EmployeeDTO getEmployeeById(String employeeId);
    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);
    EmployeeDTO updateEmployee(String employeeId, EmployeeDTO employeeDTO);
    void deleteEmployee(String employeeId);
}
