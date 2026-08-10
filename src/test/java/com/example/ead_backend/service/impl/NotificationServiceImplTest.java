package com.example.ead_backend.service.impl;

import com.example.ead_backend.dto.NotificationDTO;
import com.example.ead_backend.mapper.NotificationMapper;
import com.example.ead_backend.model.entity.Notification;
import com.example.ead_backend.model.enums.NotificationType;
import com.example.ead_backend.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationMapper notificationMapper;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private Notification notification;
    private NotificationDTO notificationDTO;

    @BeforeEach
    void setUp() {
        notification = Notification.builder()
                .id(1L)
                .userId(100L)
                .type(NotificationType.PROGRESS_UPDATE)
                .message("Your appointment is scheduled for tomorrow")
                .isRead(false)
                .build();

        notificationDTO = new NotificationDTO();
        notificationDTO.setId(1L);
        notificationDTO.setUserId(100L);
        notificationDTO.setType(NotificationType.PROGRESS_UPDATE);
        notificationDTO.setMessage("Your appointment is scheduled for tomorrow");
        notificationDTO.setIsRead(false);
    }

    @Test
    void testCreateNotification_Success() {
        // Given
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);
        when(notificationMapper.toDto(any(Notification.class))).thenReturn(notificationDTO);

        // When
        NotificationDTO result = notificationService.createNotification(
                100L,
                NotificationType.PROGRESS_UPDATE,
                "Your appointment is scheduled for tomorrow");

        // Then
        assertNotNull(result);
        assertEquals(100L, result.getUserId());
        assertEquals(NotificationType.PROGRESS_UPDATE, result.getType());
        assertEquals("Your appointment is scheduled for tomorrow", result.getMessage());
        assertFalse(result.getIsRead());
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void testGetNotificationsForUser() {
        // Given
        List<Notification> notifications = Arrays.asList(notification);
        when(notificationRepository.findByUserIdOrderByCreatedAtDesc(100L)).thenReturn(notifications);
        when(notificationMapper.toDto(any(Notification.class))).thenReturn(notificationDTO);

        // When
        List<NotificationDTO> result = notificationService.getNotificationsForUser(100L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(100L, result.get(0).getUserId());
        verify(notificationRepository).findByUserIdOrderByCreatedAtDesc(100L);
    }

    @Test
    void testMarkAsRead_Success() {
        // Given
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        // When
        notificationService.markAsRead(1L);

        // Then
        verify(notificationRepository).findById(1L);
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void testMarkAsRead_NotificationNotFound() {
        // Given
        when(notificationRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        notificationService.markAsRead(999L);

        // Then
        verify(notificationRepository).findById(999L);
        verify(notificationRepository, never()).save(any(Notification.class));
    }

    @Test
    void testCreateNotification_DifferentTypes() {
        // Given
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);
        when(notificationMapper.toDto(any(Notification.class))).thenReturn(notificationDTO);

        // Test different notification types
        NotificationType[] types = {
                NotificationType.PROGRESS_UPDATE,
                NotificationType.STATUS_CHANGE,
                NotificationType.GENERAL
        };

        for (NotificationType type : types) {
            notificationDTO.setType(type);

            // When
            NotificationDTO result = notificationService.createNotification(
                    100L,
                    type,
                    "Test message");

            // Then
            assertNotNull(result);
        }

        verify(notificationRepository, times(3)).save(any(Notification.class));
    }
}
