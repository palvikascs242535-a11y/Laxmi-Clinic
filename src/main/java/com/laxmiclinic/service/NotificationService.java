package com.laxmiclinic.service;

import com.laxmiclinic.model.Notification;
import com.laxmiclinic.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification createNotification(
            String title,
            String message,
            Notification.NotificationType type,
            Notification.NotificationPriority priority,
            String referenceId,
            String linkUrl) {
        try {
            Notification notification = new Notification(title, message, type, priority, referenceId, linkUrl);
            return notificationRepository.save(notification);
        } catch (Exception ex) {
            logger.error("Failed to save notification: {}", ex.getMessage());
            return null;
        }
    }

    public List<Notification> getRecentNotifications() {
        return notificationRepository.findTop50ByOrderByCreatedAtDesc();
    }

    public List<Notification> getUnreadNotifications() {
        return notificationRepository.findByIsReadFalseOrderByCreatedAtDesc();
    }

    public long getUnreadCount() {
        return notificationRepository.countByIsReadFalse();
    }

    public void markAsRead(String notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }

    public void markAllAsRead() {
        List<Notification> unreadList = notificationRepository.findByIsReadFalseOrderByCreatedAtDesc();
        for (Notification notif : unreadList) {
            notif.setRead(true);
        }
        notificationRepository.saveAll(unreadList);
    }

    public void deleteNotification(String id) {
        notificationRepository.deleteById(id);
    }

    public Notification createAnnouncement(String title, String message, Notification.NotificationPriority priority) {
        Notification notification = new Notification(title, message, Notification.NotificationType.CLINIC_ANNOUNCEMENT, priority, null, null);
        return notificationRepository.save(notification);
    }
}
