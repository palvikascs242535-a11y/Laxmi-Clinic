package com.laxmiclinic.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "services")
public class ServiceItem {

    @Id
    private String id;

    private String name;
    private String category;
    private String description;
    private String icon;
    private String durationMinutes;
    private String pricePlaceholder = "Consultation Fee: ₹[Price]";
    private boolean active = true;
    private int displayOrder = 0;

    @LastModifiedDate
    private LocalDateTime updatedAt = LocalDateTime.now();

    public ServiceItem() {
    }

    public ServiceItem(String name, String category, String description, String icon, int displayOrder, boolean active) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.icon = icon;
        this.displayOrder = displayOrder;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(String durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getPricePlaceholder() {
        return pricePlaceholder;
    }

    public void setPricePlaceholder(String pricePlaceholder) {
        this.pricePlaceholder = pricePlaceholder;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
