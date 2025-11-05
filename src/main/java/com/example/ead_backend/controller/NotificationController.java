package com.example.ead_backend.controller;

import com.example.ead_backend.dto.NotificationDTO;
import com.example.ead_backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for notification operations.
 */
@RestController
@RequestMapping("/api/customer/notifications")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Get all notifications for a user.
     *
     * @param userId the user ID
     * @return list of notifications
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationDTO>> getNotifications(@PathVariable Long userId) {
        log.info("Fetching notifications for user {}", userId);
        List<NotificationDTO> notifications = notificationService.getNotificationsForUser(userId);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Get unread notifications for a user.
     *
     * @param userId the user ID
     * @return list of unread notifications
     */
    @GetMapping("/{userId}/unread")
    public ResponseEntity<List<NotificationDTO>> getUnreadNotifications(@PathVariable Long userId) {
        log.info("Fetching unread notifications for user {}", userId);
        List<NotificationDTO> notifications = notificationService.getUnreadNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Mark notification as read.
     *
     * @param notificationId the notification ID
     * @return success response
     */
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<String> markAsRead(@PathVariable Long notificationId) {
        log.info("Marking notification {} as read", notificationId);
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok("Notification marked as read");
    }

    /**
     * Mark all notifications as read for a user.
     *
     * @param userId the user ID
     * @return success response
     */
    @PutMapping("/user/{userId}/read-all")
    public ResponseEntity<String> markAllAsRead(@PathVariable Long userId) {
        log.info("Marking all notifications as read for user {}", userId);
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok("All notifications marked as read");
    }

    /**
     * Delete a notification.
     *
     * @param notificationId the notification ID
     * @return success response
     */
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<String> deleteNotification(@PathVariable Long notificationId) {
        log.info("Deleting notification {}", notificationId);
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok("Notification deleted successfully");
    }

    /**
     * Delete all read notifications for a user.
     *
     * @param userId the user ID
     * @return success response
     */
    @DeleteMapping("/user/{userId}/read")
    public ResponseEntity<String> deleteReadNotifications(@PathVariable Long userId) {
        log.info("Deleting read notifications for user {}", userId);
        notificationService.deleteReadNotifications(userId);
        return ResponseEntity.ok("Read notifications deleted successfully");
    }
}
