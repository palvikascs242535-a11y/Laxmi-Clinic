package com.laxmiclinic.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "contact_submissions")
public class ContactSubmission {

    public enum InquiryStatus {
        NEW,
        IN_PROGRESS,
        RESOLVED
    }

    @Id
    private String id;

    private String name;
    private String email;
    private String mobile;
    private String subject;
    private String message;

    private InquiryStatus status = InquiryStatus.NEW;
    private String adminReply;

    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

    public ContactSubmission() {
    }

    public ContactSubmission(String name, String email, String mobile, String subject, String message) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.subject = subject;
        this.message = message;
        this.status = InquiryStatus.NEW;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public InquiryStatus getStatus() {
        return status;
    }

    public void setStatus(InquiryStatus status) {
        this.status = status;
    }

    public String getAdminReply() {
        return adminReply;
    }

    public void setAdminReply(String adminReply) {
        this.adminReply = adminReply;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
