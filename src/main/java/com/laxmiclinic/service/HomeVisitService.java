package com.laxmiclinic.service;

import com.laxmiclinic.dto.request.HomeVisitBookingRequest;
import com.laxmiclinic.exception.BadRequestException;
import com.laxmiclinic.exception.ResourceNotFoundException;
import com.laxmiclinic.model.HomeVisit;
import com.laxmiclinic.model.Notification;
import com.laxmiclinic.repository.HomeVisitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Service
public class HomeVisitService {

    private static final Logger logger = LoggerFactory.getLogger(HomeVisitService.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Standard available time slots for home visits
    public static final List<String> AVAILABLE_TIME_SLOTS = List.of(
            "07:30 AM - 09:00 AM (Early Morning)",
            "09:30 AM - 11:30 AM (Morning)",
            "12:00 PM - 02:00 PM (Afternoon)",
            "02:30 PM - 04:30 PM (Late Afternoon)",
            "05:00 PM - 07:00 PM (Evening)"
    );

    private final HomeVisitRepository homeVisitRepository;
    private final NotificationService notificationService;

    public HomeVisitService(HomeVisitRepository homeVisitRepository, NotificationService notificationService) {
        this.homeVisitRepository = homeVisitRepository;
        this.notificationService = notificationService;
    }

    public HomeVisit bookHomeVisit(HomeVisitBookingRequest request) {
        // Validate date is not in past
        try {
            LocalDate requestedDate = LocalDate.parse(request.getPreferredDate().trim(), DATE_FORMATTER);
            if (requestedDate.isBefore(LocalDate.now())) {
                throw new BadRequestException("Preferred date cannot be in the past.");
            }
        } catch (Exception ex) {
            if (ex instanceof BadRequestException) throw ex;
            throw new BadRequestException("Invalid date format. Expected YYYY-MM-DD.");
        }

        // Prevent double booking for already accepted visit at same date and slot
        boolean isSlotOccupied = homeVisitRepository.existsByPreferredDateAndPreferredTimeSlotAndStatus(
                request.getPreferredDate().trim(),
                request.getPreferredTimeSlot().trim(),
                HomeVisit.VisitStatus.ACCEPTED
        );

        if (isSlotOccupied) {
            throw new BadRequestException("The selected time slot (" + request.getPreferredTimeSlot() + ") on " + request.getPreferredDate() + " is already booked. Please choose another time slot or date.");
        }

        HomeVisit homeVisit = new HomeVisit(
                request.getPatientName().trim(),
                request.getMobile().trim(),
                request.getEmail() != null ? request.getEmail().trim() : null,
                request.getAddress().trim(),
                request.getLandmark() != null ? request.getLandmark().trim() : null,
                request.getPincode().trim(),
                request.getPreferredDate().trim(),
                request.getPreferredTimeSlot().trim(),
                request.getReason()
        );

        HomeVisit saved = homeVisitRepository.save(homeVisit);
        logger.info("New Home Visit Request created for {} on {} at {}", saved.getPatientName(), saved.getPreferredDate(), saved.getPreferredTimeSlot());

        // Trigger notification
        notificationService.createNotification(
                "New Home Visit Request",
                "Patient " + saved.getPatientName() + " requested home visit on " + saved.getPreferredDate() + " (" + saved.getPreferredTimeSlot() + ") at " + saved.getAddress(),
                Notification.NotificationType.HOME_VISIT,
                Notification.NotificationPriority.HIGH,
                saved.getId(),
                "/admin.html#homevisits"
        );

        return saved;
    }

    public List<HomeVisit> getHomeVisitsByMobile(String mobile) {
        return homeVisitRepository.findByMobileOrderByCreatedAtDesc(mobile.trim());
    }

    public List<HomeVisit> getAllHomeVisits(HomeVisit.VisitStatus status, String date) {
        if (date != null && !date.isBlank()) {
            return homeVisitRepository.findByPreferredDateOrderByCreatedAtAsc(date.trim());
        }
        if (status != null) {
            return homeVisitRepository.findByStatusOrderByCreatedAtDesc(status);
        }
        return homeVisitRepository.findAllByOrderByCreatedAtDesc();
    }

    public HomeVisit getHomeVisitById(String id) {
        return homeVisitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Home visit request not found with id: " + id));
    }

    public HomeVisit updateVisitStatus(String id, HomeVisit.VisitStatus newStatus, String adminRemark, String doctorNotes) {
        HomeVisit visit = getHomeVisitById(id);

        // If accepting, ensure no other accepted visit for that same slot
        if (newStatus == HomeVisit.VisitStatus.ACCEPTED) {
            boolean alreadyAccepted = homeVisitRepository.existsByPreferredDateAndPreferredTimeSlotAndStatus(
                    visit.getPreferredDate(),
                    visit.getPreferredTimeSlot(),
                    HomeVisit.VisitStatus.ACCEPTED
            );
            if (alreadyAccepted && !visit.getStatus().equals(HomeVisit.VisitStatus.ACCEPTED)) {
                throw new BadRequestException("Another request for date " + visit.getPreferredDate() + " and time slot " + visit.getPreferredTimeSlot() + " is already ACCEPTED.");
            }
        }

        visit.setStatus(newStatus);
        if (adminRemark != null) visit.setAdminRemark(adminRemark);
        if (doctorNotes != null) visit.setDoctorNotes(doctorNotes);
        visit.setUpdatedAt(LocalDateTime.now());

        HomeVisit saved = homeVisitRepository.save(visit);

        // Notification for status change
        notificationService.createNotification(
                "Home Visit " + newStatus.name(),
                "Home visit for " + saved.getPatientName() + " on " + saved.getPreferredDate() + " was marked as " + newStatus.name(),
                Notification.NotificationType.HOME_VISIT,
                Notification.NotificationPriority.NORMAL,
                saved.getId(),
                "/admin.html#homevisits"
        );

        return saved;
    }

    public List<String> getAvailableTimeSlots() {
        return AVAILABLE_TIME_SLOTS;
    }

    public long getPendingCount() {
        return homeVisitRepository.countByStatus(HomeVisit.VisitStatus.PENDING);
    }

    public long getAcceptedTodayCount() {
        String today = LocalDate.now().format(DATE_FORMATTER);
        return homeVisitRepository.countByPreferredDateAndStatus(today, HomeVisit.VisitStatus.ACCEPTED);
    }
}
