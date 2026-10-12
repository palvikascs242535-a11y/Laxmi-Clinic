package com.laxmiclinic.service;

import com.laxmiclinic.dto.request.ClinicInfoRequest;
import com.laxmiclinic.model.ClinicInfo;
import com.laxmiclinic.repository.ClinicInfoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ClinicInfoService {

    private final ClinicInfoRepository clinicInfoRepository;
    private final NotificationService notificationService;

    public ClinicInfoService(ClinicInfoRepository clinicInfoRepository, NotificationService notificationService) {
        this.clinicInfoRepository = clinicInfoRepository;
        this.notificationService = notificationService;
    }

    public ClinicInfo getClinicInfo() {
        return clinicInfoRepository.findFirstByOrderByIdAsc()
                .orElseGet(this::initDefaultClinicInfo);
    }

    public ClinicInfo updateClinicInfo(ClinicInfoRequest request) {
        ClinicInfo info = getClinicInfo();

        if (request.getClinicName() != null) info.setClinicName(request.getClinicName());
        if (request.getTagline() != null) info.setTagline(request.getTagline());
        if (request.getAboutDescription() != null) info.setAboutDescription(request.getAboutDescription());
        if (request.getDoctorName() != null) info.setDoctorName(request.getDoctorName());
        if (request.getDoctorQualification() != null) info.setDoctorQualification(request.getDoctorQualification());
        if (request.getDoctorSpecialty() != null) info.setDoctorSpecialty(request.getDoctorSpecialty());
        if (request.getDoctorExperience() != null) info.setDoctorExperience(request.getDoctorExperience());
        if (request.getDoctorBio() != null) info.setDoctorBio(request.getDoctorBio());
        if (request.getStreetAddress() != null) info.setStreetAddress(request.getStreetAddress());
        if (request.getCity() != null) info.setCity(request.getCity());
        if (request.getState() != null) info.setState(request.getState());
        if (request.getPincode() != null) info.setPincode(request.getPincode());
        if (request.getLandmark() != null) info.setLandmark(request.getLandmark());
        if (request.getMapEmbedUrl() != null) info.setMapEmbedUrl(request.getMapEmbedUrl());
        if (request.getPrimaryPhone() != null) info.setPrimaryPhone(request.getPrimaryPhone());
        if (request.getEmergencyPhone() != null) info.setEmergencyPhone(request.getEmergencyPhone());
        if (request.getEmail() != null) info.setEmail(request.getEmail());
        if (request.getGeneralTimings() != null) info.setGeneralTimings(request.getGeneralTimings());
        if (request.getOperational() != null) info.setOperational(request.getOperational());
        if (request.getOperationalStatusMessage() != null) info.setOperationalStatusMessage(request.getOperationalStatusMessage());
        if (request.getNoticeBanner() != null) info.setNoticeBanner(request.getNoticeBanner());
        if (request.getHighlights() != null && !request.getHighlights().isEmpty()) info.setHighlights(request.getHighlights());

        info.setUpdatedAt(LocalDateTime.now());
        return clinicInfoRepository.save(info);
    }

    public ClinicInfo toggleOperationalStatus(boolean operational, String statusMessage) {
        ClinicInfo info = getClinicInfo();
        info.setOperational(operational);
        if (statusMessage != null && !statusMessage.isBlank()) {
            info.setOperationalStatusMessage(statusMessage);
        } else {
            info.setOperationalStatusMessage(operational ? "Clinic is currently open and accepting patients." : "Clinic is currently closed.");
        }
        info.setUpdatedAt(LocalDateTime.now());
        return clinicInfoRepository.save(info);
    }

    public ClinicInfo initDefaultClinicInfo() {
        clinicInfoRepository.deleteAll();
        ClinicInfo info = new ClinicInfo();
        return clinicInfoRepository.save(info);
    }
}