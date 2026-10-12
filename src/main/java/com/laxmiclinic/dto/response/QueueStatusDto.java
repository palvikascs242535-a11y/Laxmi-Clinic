package com.laxmiclinic.dto.response;

import java.util.List;

public class QueueStatusDto {

    private String todayDate;
    private int currentTokenNumber;
    private String currentTokenStatus;
    private String currentPatientMaskedName;
    private String currentShiftName;
    private int waitingCount;
    private int calledCount;
    private int completedCount;
    private int skippedCount;
    private int totalTokensToday;
    private boolean clinicOperational;
    private String operationalMessage;
    private String noticeBanner;
    private List<TokenResponseDto> recentTokens;

    public QueueStatusDto() {
    }

    public String getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(String todayDate) {
        this.todayDate = todayDate;
    }

    public int getCurrentTokenNumber() {
        return currentTokenNumber;
    }

    public void setCurrentTokenNumber(int currentTokenNumber) {
        this.currentTokenNumber = currentTokenNumber;
    }

    public String getCurrentTokenStatus() {
        return currentTokenStatus;
    }

    public void setCurrentTokenStatus(String currentTokenStatus) {
        this.currentTokenStatus = currentTokenStatus;
    }

    public String getCurrentPatientMaskedName() {
        return currentPatientMaskedName;
    }

    public void setCurrentPatientMaskedName(String currentPatientMaskedName) {
        this.currentPatientMaskedName = currentPatientMaskedName;
    }

    public String getCurrentShiftName() {
        return currentShiftName;
    }

    public void setCurrentShiftName(String currentShiftName) {
        this.currentShiftName = currentShiftName;
    }

    public int getWaitingCount() {
        return waitingCount;
    }

    public void setWaitingCount(int waitingCount) {
        this.waitingCount = waitingCount;
    }

    public int getCalledCount() {
        return calledCount;
    }

    public void setCalledCount(int calledCount) {
        this.calledCount = calledCount;
    }

    public int getCompletedCount() {
        return completedCount;
    }

    public void setCompletedCount(int completedCount) {
        this.completedCount = completedCount;
    }

    public int getSkippedCount() {
        return skippedCount;
    }

    public void setSkippedCount(int skippedCount) {
        this.skippedCount = skippedCount;
    }

    public int getTotalTokensToday() {
        return totalTokensToday;
    }

    public void setTotalTokensToday(int totalTokensToday) {
        this.totalTokensToday = totalTokensToday;
    }

    public boolean isClinicOperational() {
        return clinicOperational;
    }

    public void setClinicOperational(boolean clinicOperational) {
        this.clinicOperational = clinicOperational;
    }

    public String getOperationalMessage() {
        return operationalMessage;
    }

    public void setOperationalMessage(String operationalMessage) {
        this.operationalMessage = operationalMessage;
    }

    public String getNoticeBanner() {
        return noticeBanner;
    }

    public void setNoticeBanner(String noticeBanner) {
        this.noticeBanner = noticeBanner;
    }

    public List<TokenResponseDto> getRecentTokens() {
        return recentTokens;
    }

    public void setRecentTokens(List<TokenResponseDto> recentTokens) {
        this.recentTokens = recentTokens;
    }
}
