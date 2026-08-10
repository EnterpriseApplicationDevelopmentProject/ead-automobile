package com.example.ead_backend.service.impl;

import com.example.ead_backend.dto.ProjectDTO;
import com.example.ead_backend.mapper.ProjectMapper;
import com.example.ead_backend.model.entity.Employee;
import com.example.ead_backend.model.entity.Project;
import com.example.ead_backend.model.enums.ProjectStatus;
import com.example.ead_backend.repository.EmployeeRepository;
import com.example.ead_backend.repository.ProjectRepository;
import com.example.ead_backend.service.ProgressCalculationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ProjectMapper projectMapper;

    @Mock
    private ProgressCalculationService progressCalculationService;

    @InjectMocks
    private ProjectServiceImpl projectService;

    private ProjectDTO projectDTO;
    private Project project;

    @BeforeEach
    void setUp() {
        projectDTO = new ProjectDTO();
        projectDTO.setProjectId("PRJ001");
        projectDTO.setName("Engine Overhaul");
        projectDTO.setDescription("Complete engine overhaul for customer vehicle");
        projectDTO.setCustomerId("CUST001");
        projectDTO.setStartDate(LocalDate.of(2024, 1, 1));
        projectDTO.setEndDate(LocalDate.of(2024, 3, 31));
        projectDTO.setStatus(ProjectStatus.IN_PROGRESS);

        project = new Project();
        project.setProjectId("PRJ001");
        project.setName("Engine Overhaul");
        project.setDescription("Complete engine overhaul for customer vehicle");
        project.setCustomerId("CUST001");
        project.setStartDate(LocalDate.of(2024, 1, 1));
        project.setEndDate(LocalDate.of(2024, 3, 31));
        project.setStatus(ProjectStatus.IN_PROGRESS);
    }

    @Test
    void testCreateProject_Success() {
        // Given
        when(projectMapper.toEntity(any(ProjectDTO.class))).thenReturn(project);
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        when(projectMapper.toDTO(any(Project.class))).thenReturn(projectDTO);

        // When
        ProjectDTO result = projectService.createProject(projectDTO);

        // Then
        assertNotNull(result);
        assertEquals("PRJ001", result.getProjectId());
        assertEquals("Engine Overhaul", result.getName());
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    void testGetProjectById_Success() {
        // Given
        when(projectRepository.findById("PRJ001")).thenReturn(Optional.of(project));
        when(projectMapper.toDTO(project)).thenReturn(projectDTO);

        // When
        ProjectDTO result = projectService.getProjectById("PRJ001");

        // Then
        assertNotNull(result);
        assertEquals("PRJ001", result.getProjectId());
        verify(projectRepository).findById("PRJ001");
    }

    @Test
    void testGetProjectById_NotFound() {
        // Given
        when(projectRepository.findById("INVALID")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> projectService.getProjectById("INVALID"));
    }

    @Test
    void testGetAllProjects() {
        // Given
        List<Project> projects = Arrays.asList(project);
        when(projectRepository.findAll()).thenReturn(projects);
        when(projectMapper.toDTO(any(Project.class))).thenReturn(projectDTO);
        when(progressCalculationService.getLatestProgress(anyString())).thenReturn(45);

        // When
        List<ProjectDTO> result = projectService.getAllProjects();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(projectRepository).findAll();
    }

    @Test
    void testGetProjectsByCustomerId() {
        // Given
        List<Project> projects = Arrays.asList(project);
        when(projectRepository.findByCustomerId("CUST001")).thenReturn(projects);
        when(projectMapper.toDTO(any(Project.class))).thenReturn(projectDTO);
        when(progressCalculationService.getLatestProgress(anyString())).thenReturn(60);

        // When
        List<ProjectDTO> result = projectService.getProjectsByCustomerId("CUST001");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(projectRepository).findByCustomerId("CUST001");
    }

    @Test
    void testUpdateProject_Success() {
        // Given
        when(projectRepository.findById("PRJ001")).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        when(projectMapper.toDTO(any(Project.class))).thenReturn(projectDTO);

        // When
        ProjectDTO result = projectService.updateProject("PRJ001", projectDTO);

        // Then
        assertNotNull(result);
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    void testDeleteProject() {
        // When
        projectService.deleteProject("PRJ001");

        // Then
        verify(projectRepository).deleteById("PRJ001");
    }

    @Test
    void testAssignEmployeeToProject_Success() {
        // Given
        Employee employee = new Employee();
        employee.setId(1L);

        when(projectRepository.findById("PRJ001")).thenReturn(Optional.of(project));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        when(projectMapper.toDTO(any(Project.class))).thenReturn(projectDTO);

        // When
        ProjectDTO result = projectService.assignEmployeeToProject("PRJ001", 1L);

        // Then
        assertNotNull(result);
        verify(projectRepository).save(any(Project.class));
        verify(employeeRepository).findById(1L);
    }

    @Test
    void testGetProjectsByEmployeeId() {
        // Given
        List<Project> projects = Arrays.asList(project);
        when(projectRepository.findByEmployeeId(1L)).thenReturn(projects);
        when(projectMapper.toDTO(any(Project.class))).thenReturn(projectDTO);
        when(progressCalculationService.getLatestProgress(anyString())).thenReturn(70);

        // When
        List<ProjectDTO> result = projectService.getProjectsByEmployeeId(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(projectRepository).findByEmployeeId(1L);
    }
}
