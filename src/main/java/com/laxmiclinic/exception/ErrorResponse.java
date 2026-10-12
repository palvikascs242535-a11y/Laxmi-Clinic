package com.laxmiclinic.exception;

import java.time.LocalDateTime;
import java.util.Map;

public class ErrorResponse {
    private boolean success = false;
    private String message;
    private int status;
    private LocalDateTime timestamp = LocalDateTime.now();
    private Map<String, String> errors;

    public ErrorResponse() {
    }

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.success = false;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(int status, String message, Map<String, String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.success = false;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
