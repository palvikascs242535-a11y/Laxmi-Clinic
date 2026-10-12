package com.laxmiclinic.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "tokens")
@CompoundIndex(name = "date_token_idx", def = "{'tokenDate': 1, 'tokenNumber': 1}")
public class Token {

    public enum TokenStatus {
        WAITING,
        CALLED,
        COMPLETED,
        SKIPPED,
        CANCELLED
    }

    @Id
    private String id;

    @Indexed
    private int tokenNumber; // 1, 2, 3... per day

    @Indexed
    private String tokenDate; // "YYYY-MM-DD"

    private String patientName;
    
    @Indexed
    private String patientPhone;

    private Integer age;
    private String gender;
    private String symptoms;
    private String notes;

    private int shiftNumber; // 1 to 5
    private String shiftName;

    private TokenStatus status = TokenStatus.WAITING;

    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime calledAt;
    private LocalDateTime completedAt;
    private LocalDateTime skippedAt;

    private String adminRemark;

    public Token() {
    }

    public Token(int tokenNumber, String tokenDate, String patientName, String patientPhone, Integer age, String gender, String symptoms, int shiftNumber, String shiftName) {
        this.tokenNumber = tokenNumber;
        this.tokenDate = tokenDate;
        this.patientName = patientName;
        this.patientPhone = patientPhone;
        this.age = age;
        this.gender = gender;
        this.symptoms = symptoms;
        this.shiftNumber = shiftNumber;
        this.shiftName = shiftName;
        this.status = TokenStatus.WAITING;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public TokenStatus getStatus() {
        return status;
    }

    public void setStatus(TokenStatus status) {
        this.status = status;
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

    public LocalDateTime getSkippedAt() {
        return skippedAt;
    }

    public void setSkippedAt(LocalDateTime skippedAt) {
        this.skippedAt = skippedAt;
    }

    public String getAdminRemark() {
        return adminRemark;
    }

    public void setAdminRemark(String adminRemark) {
        this.adminRemark = adminRemark;
    }
}
