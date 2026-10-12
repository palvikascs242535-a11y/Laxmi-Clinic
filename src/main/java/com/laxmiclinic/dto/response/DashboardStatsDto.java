package com.laxmiclinic.dto.response;

import com.laxmiclinic.model.Shift;
import java.util.List;

public class DashboardStatsDto {

    // Token metrics
    private int todayTotalTokens;
    private int currentTokenNumber;
    private String currentTokenPatientName;
    private int waitingPatientsCount;
    private int completedPatientsCount;
    private int skippedPatientsCount;

    // Home visit metrics
    private int pendingHomeVisitsCount;
    private int acceptedHomeVisitsTodayCount;
    private int totalHomeVisits;

    // Notifications & Contact metrics
    private int unreadNotificationsCount;
    private int newContactInquiriesCount;

    // Clinic operational & Shift metrics
    private boolean clinicOperational;
    private String operationalStatusMessage;
    private List<Shift> activeShifts;

    public DashboardStatsDto() {
    }

    public int getTodayTotalTokens() {
        return todayTotalTokens;
    }

    public void setTodayTotalTokens(int todayTotalTokens) {
        this.todayTotalTokens = todayTotalTokens;
    }

    public int getCurrentTokenNumber() {
        return currentTokenNumber;
    }

    public void setCurrentTokenNumber(int currentTokenNumber) {
        this.currentTokenNumber = currentTokenNumber;
    }

    public String getCurrentTokenPatientName() {
        return currentTokenPatientName;
    }

    public void setCurrentTokenPatientName(String currentTokenPatientName) {
        this.currentTokenPatientName = currentTokenPatientName;
    }

    public int getWaitingPatientsCount() {
        return waitingPatientsCount;
    }

    public void setWaitingPatientsCount(int waitingPatientsCount) {
        this.waitingPatientsCount = waitingPatientsCount;
    }

    public int getCompletedPatientsCount() {
        return completedPatientsCount;
    }

    public void setCompletedPatientsCount(int completedPatientsCount) {
        this.completedPatientsCount = completedPatientsCount;
    }

    public int getSkippedPatientsCount() {
        return skippedPatientsCount;
    }

    public void setSkippedPatientsCount(int skippedPatientsCount) {
        this.skippedPatientsCount = skippedPatientsCount;
    }

    public int getPendingHomeVisitsCount() {
        return pendingHomeVisitsCount;
    }

    public void setPendingHomeVisitsCount(int pendingHomeVisitsCount) {
        this.pendingHomeVisitsCount = pendingHomeVisitsCount;
    }

    public int getAcceptedHomeVisitsTodayCount() {
        return acceptedHomeVisitsTodayCount;
    }

    public void setAcceptedHomeVisitsTodayCount(int acceptedHomeVisitsTodayCount) {
        this.acceptedHomeVisitsTodayCount = acceptedHomeVisitsTodayCount;
    }

    public int getTotalHomeVisits() {
        return totalHomeVisits;
    }

    public void setTotalHomeVisits(int totalHomeVisits) {
        this.totalHomeVisits = totalHomeVisits;
    }

    public int getUnreadNotificationsCount() {
        return unreadNotificationsCount;
    }

    public void setUnreadNotificationsCount(int unreadNotificationsCount) {
        this.unreadNotificationsCount = unreadNotificationsCount;
    }

    public int getNewContactInquiriesCount() {
        return newContactInquiriesCount;
    }

    public void setNewContactInquiriesCount(int newContactInquiriesCount) {
        this.newContactInquiriesCount = newContactInquiriesCount;
    }

    public boolean isClinicOperational() {
        return clinicOperational;
    }

    public void setClinicOperational(boolean clinicOperational) {
        this.clinicOperational = clinicOperational;
    }

    public String getOperationalStatusMessage() {
        return operationalStatusMessage;
    }

    public void setOperationalStatusMessage(String operationalStatusMessage) {
        this.operationalStatusMessage = operationalStatusMessage;
    }

    public List<Shift> getActiveShifts() {
        return activeShifts;
    }

    public void setActiveShifts(List<Shift> activeShifts) {
        this.activeShifts = activeShifts;
    }
}