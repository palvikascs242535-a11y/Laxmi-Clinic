package com.laxmiclinic.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class ShiftRequest {

    @Min(value = 1, message = "Shift number must be between 1 and 5")
    @Max(value = 5, message = "Shift number must be between 1 and 5")
    private int shiftNumber;

    @NotBlank(message = "Shift name is required")
    private String name;

    @NotBlank(message = "Start time is required")
    private String startTime;

    @NotBlank(message = "End time is required")
    private String endTime;

    private String description;
    private int maxTokensCapacity = 30;
    private boolean active = true;

    public ShiftRequest() {
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
}
