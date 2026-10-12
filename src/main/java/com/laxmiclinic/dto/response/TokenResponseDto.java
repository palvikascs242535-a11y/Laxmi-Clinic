package com.laxmiclinic.dto.response;

import com.laxmiclinic.model.Token;

import java.time.LocalDateTime;

public class TokenResponseDto {

    private String id;
    private int tokenNumber;
    private String tokenDate;
    private String patientName;
    private String patientPhone;
    private Integer age;
    private String gender;
    private String symptoms;
    private int shiftNumber;
    private String shiftName;
    private Token.TokenStatus status;
    private int currentTokenNumber;
    private int patientsAhead;
    private int estimatedWaitMinutes;
    private LocalDateTime createdAt;
    private LocalDateTime calledAt;
    private LocalDateTime completedAt;
    private String statusMessage;

    public TokenResponseDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTokenNumber() {
        return tokenNumber;
    }

    public void setTokenNumber(int tokenNumber) {
        this.tokenNumber = tokenNumber;
    }

    public String getTokenDate() {
        return tokenDate;
    }

    public void setTokenDate(String tokenDate) {
        this.tokenDate = tokenDate;
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

    public int getShiftNumber() {
        return shiftNumber;
    }

    public void setShiftNumber(int shiftNumber) {
        this.shiftNumber = shiftNumber;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public Token.TokenStatus getStatus() {
        return status;
    }

    public void setStatus(Token.TokenStatus status) {
        this.status = status;
    }

    public int getCurrentTokenNumber() {
        return currentTokenNumber;
    }

    public void setCurrentTokenNumber(int currentTokenNumber) {
        this.currentTokenNumber = currentTokenNumber;
    }

    public int getPatientsAhead() {
        return patientsAhead;
    }

    public void setPatientsAhead(int patientsAhead) {
        this.patientsAhead = patientsAhead;
    }

    public int getEstimatedWaitMinutes() {
        return estimatedWaitMinutes;
    }

    public void setEstimatedWaitMinutes(int estimatedWaitMinutes) {
        this.estimatedWaitMinutes = estimatedWaitMinutes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCalledAt() {
        return calledAt;
    }

    public void setCalledAt(LocalDateTime calledAt) {
        this.calledAt = calledAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
