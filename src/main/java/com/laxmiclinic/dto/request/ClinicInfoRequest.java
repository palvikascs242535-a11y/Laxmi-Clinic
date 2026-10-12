package com.laxmiclinic.dto.request;

import java.util.List;

public class ClinicInfoRequest {
    private String clinicName;
    private String tagline;
    private String aboutDescription;
    private String doctorName;
    private String doctorQualification;
    private String doctorSpecialty;
    private String doctorExperience;
    private String doctorBio;
    private String streetAddress;
    private String city;
    private String state;
    private String pincode;
    private String landmark;
    private String mapEmbedUrl;
    private String primaryPhone;
    private String emergencyPhone;
    private String email;
    private String generalTimings;
    private Boolean operational;
    private String operationalStatusMessage;
    private String noticeBanner;
    private List<String> highlights;

    public ClinicInfoRequest() {
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

    public Boolean getOperational() {
        return operational;
    }

    public void setOperational(Boolean operational) {
        this.operational = operational;
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
}
