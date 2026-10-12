package com.laxmiclinic.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class TokenGenerateRequest {

    @NotBlank(message = "Patient name is required")
    private String patientName;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[0-9+\\-\\s()]{7,15}$", message = "Invalid mobile number format")
    private String patientPhone;

    private Integer age;
    private String gender;
    private String symptoms;
    private Integer shiftNumber; // Optional, defaults to current active shift

    public TokenGenerateRequest() {
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public Integer getShiftNumber() {
        return shiftNumber;
    }

    public void setShiftNumber(Integer shiftNumber) {
        this.shiftNumber = shiftNumber;
    }
}
