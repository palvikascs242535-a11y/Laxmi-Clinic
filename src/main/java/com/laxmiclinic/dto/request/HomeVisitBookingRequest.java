package com.laxmiclinic.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class HomeVisitBookingRequest {

    @NotBlank(message = "Patient name is required")
    private String patientName;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[0-9+\\-\\s()]{7,15}$", message = "Invalid mobile number format")
    private String mobile;

    private String email;

    @NotBlank(message = "Full address is required")
    private String address;

    private String landmark;

    @NotBlank(message = "Pincode is required")
    private String pincode;

    @NotBlank(message = "Preferred date is required")
    private String preferredDate; // "YYYY-MM-DD"

    @NotBlank(message = "Preferred time slot is required")
    private String preferredTimeSlot; // e.g. "10:00 AM - 12:00 PM"

    private String reason;

    public HomeVisitBookingRequest() {
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPreferredDate() {
        return preferredDate;
    }

    public void setPreferredDate(String preferredDate) {
        this.preferredDate = preferredDate;
    }

    public String getPreferredTimeSlot() {
        return preferredTimeSlot;
    }

    public void setPreferredTimeSlot(String preferredTimeSlot) {
        this.preferredTimeSlot = preferredTimeSlot;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
