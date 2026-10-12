package com.laxmiclinic.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "notifications")
public class Notification {

    public enum NotificationType {
        TOKEN_ALERT,
        HOME_VISIT,
        CONTACT_INQUIRY,
        CLINIC_ANNOUNCEMENT,
        SYSTEM
    }

    public enum NotificationPriority {
        LOW,
        NORMAL,
        HIGH,
        URGENT
    }

    @Id
    private String id;

    private String title;
    private String message;
    private NotificationType type = NotificationType.SYSTEM;
    private NotificationPriority priority = NotificationPriority.NORMAL;
    
    private boolean isRead = false;
    private String targetRole = "ROLE_ADMIN"; // "ROLE_ADMIN" or "PUBLIC"
    private String referenceId; // e.g. tokenId, homeVisitId, contactId
    private String linkUrl;

    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

    public Notification() {
    }

    public Notification(String title, String message, NotificationType type, NotificationPriority priority, String referenceId, String linkUrl) {
        this.title = title;
        this.message = message;
        this.type = type;
        this.priority = priority;
        this.referenceId = referenceId;
        this.linkUrl = linkUrl;
        this.isRead = false;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public NotificationPriority getPriority() {
        return priority;
    }

    public void setPriority(NotificationPriority priority) {
        this.priority = priority;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getTargetRole() {
        return targetRole;
    }

    public void setTargetRole(String targetRole) {
        this.targetRole = targetRole;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
