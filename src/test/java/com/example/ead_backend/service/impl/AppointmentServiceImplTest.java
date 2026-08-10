package com.example.ead_backend.service.impl;

import com.example.ead_backend.dto.AppointmentDTO;
import com.example.ead_backend.mapper.AppointmentMapper;
import com.example.ead_backend.model.entity.Appointment;
import com.example.ead_backend.model.entity.Employee;
import com.example.ead_backend.model.entity.TimeLog;
import com.example.ead_backend.model.enums.AppointmentStatus;
import com.example.ead_backend.repository.AppointmentRepository;
import com.example.ead_backend.repository.EmployeeRepository;
import com.example.ead_backend.repository.TimeLogRepository;
import com.example.ead_backend.service.ProgressCalculationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private TimeLogRepository timeLogRepository;

    @Mock
    private AppointmentMapper appointmentMapper;

    @Mock
    private ProgressCalculationService progressCalculationService;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    private AppointmentDTO appointmentDTO;
    private Appointment appointment;

    @BeforeEach
    void setUp() {
        appointmentDTO = new AppointmentDTO();
        appointmentDTO.setAppointmentId("APT001");
        appointmentDTO.setService("Oil Change");
        appointmentDTO.setCustomerId("CUST001");
        appointmentDTO.setVehicleId("VEH001");
        appointmentDTO.setVehicleNo("ABC-1234");
        appointmentDTO.setDate(LocalDate.of(2024, 1, 15));
        appointmentDTO.setStartTime("09:00");
        appointmentDTO.setEndTime("10:00");
        appointmentDTO.setStatus(AppointmentStatus.UPCOMING);

        appointment = new Appointment();
        appointment.setAppointmentId("APT001");
        appointment.setService("Oil Change");
        appointment.setCustomerId("CUST001");
        appointment.setVehicleId("VEH001");
        appointment.setVehicleNo("ABC-1234");
        appointment.setDate(LocalDate.of(2024, 1, 15));
        appointment.setStartTime("09:00");
        appointment.setEndTime("10:00");
        appointment.setStatus(AppointmentStatus.UPCOMING);
    }

    @Test
    void testCreateAppointment_Success() {
        // Given
        when(appointmentRepository.existsByDateAndStartTimeAndStatusNot(
                any(LocalDate.class), anyString(), eq(AppointmentStatus.CANCELLED)))
                .thenReturn(false);
        when(appointmentMapper.toEntity(any(AppointmentDTO.class))).thenReturn(appointment);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);
        when(appointmentMapper.toDTO(any(Appointment.class))).thenReturn(appointmentDTO);

        // When
        AppointmentDTO result = appointmentService.createAppointment(appointmentDTO);

        // Then
        assertNotNull(result);
        assertEquals("APT001", result.getAppointmentId());
        assertEquals("Oil Change", result.getService());
        verify(appointmentRepository).save(any(Appointment.class));
    }

    @Test
    void testCreateAppointment_TimeSlotAlreadyBooked() {
        // Given
        when(appointmentRepository.existsByDateAndStartTimeAndStatusNot(
                any(LocalDate.class), anyString(), eq(AppointmentStatus.CANCELLED)))
                .thenReturn(true);

        // When & Then
        assertThrows(IllegalStateException.class, () -> appointmentService.createAppointment(appointmentDTO));
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void testGetAppointmentById_Success() {
        // Given
        when(appointmentRepository.findById("APT001")).thenReturn(Optional.of(appointment));
        when(appointmentMapper.toDTO(appointment)).thenReturn(appointmentDTO);

        // When
        AppointmentDTO result = appointmentService.getAppointmentById("APT001");

        // Then
        assertNotNull(result);
        assertEquals("APT001", result.getAppointmentId());
        verify(appointmentRepository).findById("APT001");
    }

    @Test
    void testGetAppointmentById_NotFound() {
        // Given
        when(appointmentRepository.findById("INVALID")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> appointmentService.getAppointmentById("INVALID"));
    }

    @Test
    void testGetAllAppointments() {
        // Given
        List<Appointment> appointments = Arrays.asList(appointment);
        when(appointmentRepository.findAll()).thenReturn(appointments);
        when(appointmentMapper.toDTO(any(Appointment.class))).thenReturn(appointmentDTO);
        when(progressCalculationService.getLatestProgress(anyString())).thenReturn(50);

        // When
        List<AppointmentDTO> result = appointmentService.getAllAppointments();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(appointmentRepository).findAll();
    }

    @Test
    void testGetAppointmentsByCustomerId() {
        // Given
        List<Appointment> appointments = Arrays.asList(appointment);
        when(appointmentRepository.findByCustomerId("CUST001")).thenReturn(appointments);
        when(appointmentMapper.toDTO(any(Appointment.class))).thenReturn(appointmentDTO);
        when(progressCalculationService.getLatestProgress(anyString())).thenReturn(75);

        // When
        List<AppointmentDTO> result = appointmentService.getAppointmentsByCustomerId("CUST001");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(appointmentRepository).findByCustomerId("CUST001");
    }

    @Test
    void testUpdateAppointment_Success() {
        // Given
        when(appointmentRepository.findById("APT001")).thenReturn(Optional.of(appointment));
        when(appointmentRepository.existsByDateAndStartTimeAndStatusNot(
                any(LocalDate.class), anyString(), eq(AppointmentStatus.CANCELLED)))
                .thenReturn(false);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);
        when(appointmentMapper.toDTO(any(Appointment.class))).thenReturn(appointmentDTO);

        // When
        AppointmentDTO result = appointmentService.updateAppointment("APT001", appointmentDTO);

        // Then
        assertNotNull(result);
        verify(appointmentRepository).save(any(Appointment.class));
    }

    @Test
    void testDeleteAppointment() {
        // When
        appointmentService.deleteAppointment("APT001");

        // Then
        verify(appointmentRepository).deleteById("APT001");
    }

    @Test
    void testAssignEmployeeToAppointment_Success() {
        // Given
        Employee employee = new Employee();
        employee.setId(1L);

        when(appointmentRepository.findById("APT001")).thenReturn(Optional.of(appointment));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);
        when(appointmentMapper.toDTO(any(Appointment.class))).thenReturn(appointmentDTO);

        // When
        AppointmentDTO result = appointmentService.assignEmployeeToAppointment("APT001", 1L);

        // Then
        assertNotNull(result);
        verify(appointmentRepository).save(any(Appointment.class));
        verify(employeeRepository).findById(1L);
    }

    @Test
    void testGetAppointmentsByEmployeeId() {
        // Given
        List<Appointment> appointments = Arrays.asList(appointment);
        when(appointmentRepository.findByEmployeeId(1L)).thenReturn(appointments);
        when(appointmentMapper.toDTO(any(Appointment.class))).thenReturn(appointmentDTO);
        when(progressCalculationService.getLatestProgress(anyString())).thenReturn(60);

        // When
        List<AppointmentDTO> result = appointmentService.getAppointmentsByEmployeeId(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(appointmentRepository).findByEmployeeId(1L);
    }

    @Test
    void testGetBookedStartTimes() {
        // Given
        LocalDate date = LocalDate.of(2024, 1, 15);
        List<Appointment> appointments = Arrays.asList(appointment);
        TimeLog timeLog = new TimeLog();
        timeLog.setStartTime("10:00");
        timeLog.setEndTime("11:00");
        List<TimeLog> timeLogs = Arrays.asList(timeLog);

        when(appointmentRepository.findByDate(date)).thenReturn(appointments);
        when(timeLogRepository.findByDate(date)).thenReturn(timeLogs);

        // When
        List<String> result = appointmentService.getBookedStartTimes(date);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(appointmentRepository).findByDate(date);
        verify(timeLogRepository).findByDate(date);
    }
}
