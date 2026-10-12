package com.laxmiclinic.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "clinic_info")
public class ClinicInfo {

    @Id
    private String id;

    private String clinicName = "Laxmi Clinic";
    private String tagline = "Caring for Your Health with Compassion & Excellence";
    private String aboutDescription = "Laxmi Clinic is dedicated to providing high quality, patient-centered healthcare. Led by Dr. Rajesh Sonar (B.A.M.S. BOM, ESIS Panel Doctor), our clinic offers outpatient consultations, preventive medical care, and home visit care.";
    
    // Doctor Details from Visiting Card
    private String doctorName = "Dr. Rajesh Sonar";
    private String doctorQualification = "B.A.M.S. (BOM)";
    private String doctorSpecialty = "General Physician & ESIS Panel Doctor";
    private String doctorExperience = "30+ Years Clinical Experience";
    private String doctorBio = "Dr. Rajesh Sonar is an experienced medical practitioner holding B.A.M.S. (BOM) and an approved ESIS Panel Doctor (Reg: 31-8933, Sakinaka). He provides comprehensive clinical OPD consultations, diagnosis, treatment, and dedicated community healthcare.";

    // Address & Location from Visiting Card
    private String streetAddress = "Shop No. 6, PNB, Sakinaka, Br. Kangira Bai, Parshuram Pal Niwas, Krishna Nagar, 90 Feet Road, Sakinaka";
    private String city = "Mumbai";
    private String state = "Maharashtra";
    private String pincode = "400072";
    private String landmark = "Near PNB / Br. Kangira Bai, Sakinaka";
    private String mapEmbedUrl = "";

    // Contact Information from Visiting Card
    private String primaryPhone = "9819531470";
    private String emergencyPhone = "9819531470";
    private String email = "clinic@email.com";
    private String generalTimings = "12:00 PM - 05:00 PM & 07:00 PM - 12:00 AM (Mon - Sat)";

    // Operational Status
    private boolean isOperational = true;
    private String operationalStatusMessage = "Clinic is currently open and accepting patients.";
    private String noticeBanner = "";

    private List<String> highlights = new ArrayList<>(List.of(
            "Dr. Rajesh Sonar - B.A.M.S. (BOM)",
            "Approved ESIS Panel Doctor (Reg: 31-8933 Sakinaka)",
            "OPD Consultation Timings: 12:00 PM - 05:00 PM & 07:00 PM - 12:00 AM",
            "Real-Time Online Token Queue System",
            "Convenient Doctor Home Visit Consultations"
    ));

    @LastModifiedDate
    private LocalDateTime updatedAt = LocalDateTime.now();

    public ClinicInfo() {
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getAboutDescription() {
        return aboutDescription;
    }

    public void setAboutDescription(String aboutDescription) {
        this.aboutDescription = aboutDescription;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorQualification() {
        return doctorQualification;
    }

    public void setDoctorQualification(String doctorQualification) {
        this.doctorQualification = doctorQualification;
    }

    public String getDoctorSpecialty() {
        return doctorSpecialty;
    }

    public void setDoctorSpecialty(String doctorSpecialty) {
        this.doctorSpecialty = doctorSpecialty;
    }

    public String getDoctorExperience() {
        return doctorExperience;
    }

    public void setDoctorExperience(String doctorExperience) {
        this.doctorExperience = doctorExperience;
    }

    public String getDoctorBio() {
        return doctorBio;
    }

    public void setDoctorBio(String doctorBio) {
        this.doctorBio = doctorBio;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getMapEmbedUrl() {
        return mapEmbedUrl;
    }

    public void setMapEmbedUrl(String mapEmbedUrl) {
        this.mapEmbedUrl = mapEmbedUrl;
    }

    public String getPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    public String getEmergencyPhone() {
        return emergencyPhone;
    }

    public void setEmergencyPhone(String emergencyPhone) {
        this.emergencyPhone = emergencyPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGeneralTimings() {
        return generalTimings;
    }

    public void setGeneralTimings(String generalTimings) {
        this.generalTimings = generalTimings;
    }

    public boolean isOperational() {
        return isOperational;
    }

    public void setOperational(boolean operational) {
        isOperational = operational;
    }

    public String getOperationalStatusMessage() {
        return operationalStatusMessage;
    }

    public void setOperationalStatusMessage(String operationalStatusMessage) {
        this.operationalStatusMessage = operationalStatusMessage;
    }

    public String getNoticeBanner() {
        return noticeBanner;
    }

    public void setNoticeBanner(String noticeBanner) {
        this.noticeBanner = noticeBanner;
    }

    public List<String> getHighlights() {
        return highlights;
    }

    public void setHighlights(List<String> highlights) {
        this.highlights = highlights;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}