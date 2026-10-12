package com.laxmiclinic.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "shifts")
public class Shift {

    @Id
    private String id;

    @Indexed(unique = true)
    private int shiftNumber; // 1 to 5

    private String name; // e.g. "Morning Shift"
    private String startTime; // e.g. "09:00 AM"
    private String endTime; // e.g. "11:30 AM"
    private String description;
    private int maxTokensCapacity = 30;
    private boolean active = true;

    @LastModifiedDate
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Shift() {
    }

    public Shift(int shiftNumber, String name, String startTime, String endTime, String description, boolean active) {
        this.shiftNumber = shiftNumber;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.active = active;
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getShiftNumber() {
        return shiftNumber;
    }

    public void setShiftNumber(int shiftNumber) {
        this.shiftNumber = shiftNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaxTokensCapacity() {
        return maxTokensCapacity;
    }

    public void setMaxTokensCapacity(int maxTokensCapacity) {
        this.maxTokensCapacity = maxTokensCapacity;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
