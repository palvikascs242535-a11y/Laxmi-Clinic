package com.laxmiclinic.controller.adminapi;

import com.laxmiclinic.dto.response.ApiResponse;
import com.laxmiclinic.model.Notification;
import com.laxmiclinic.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/notifications")
public class AdminNotificationController {

    private final NotificationService notificationService;

    public AdminNotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Notification>>> getNotifications() {
        List<Notification> notifications = notificationService.getRecentNotifications();
        return ResponseEntity.ok(ApiResponse.ok("Notifications retrieved", notifications));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(@PathVariable String id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok(ApiResponse.ok("Notification marked as read", null));
    }

    @PutMapping("/mark-all-read")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead() {
        notificationService.markAllAsRead();
        return ResponseEntity.ok(ApiResponse.ok("All notifications marked as read", null));
    }

    @PostMapping("/broadcast")
    public ResponseEntity<ApiResponse<Notification>> createBroadcast(@RequestBody Map<String, String> payload) {
        String title = payload.get("title");
        String message = payload.get("message");
        String priorityStr = payload.getOrDefault("priority", "NORMAL");
        Notification.NotificationPriority priority = Notification.NotificationPriority.valueOf(priorityStr.toUpperCase());

        Notification created = notificationService.createAnnouncement(title, message, priority);
        return new ResponseEntity<>(ApiResponse.ok("Announcement broadcasted successfully", created), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(@PathVariable String id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok(ApiResponse.ok("Notification deleted", null));
    }
}
