package com.laxmiclinic.controller.adminapi;

import com.laxmiclinic.dto.response.ApiResponse;
import com.laxmiclinic.dto.response.DashboardStatsDto;
import com.laxmiclinic.dto.response.QueueStatusDto;
import com.laxmiclinic.model.ClinicInfo;
import com.laxmiclinic.model.Shift;
import com.laxmiclinic.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/dashboard")
public class AdminDashboardController {

    private final TokenQueueService tokenQueueService;
    private final HomeVisitService homeVisitService;
    private final NotificationService notificationService;
    private final ContactService contactService;
    private final ClinicInfoService clinicInfoService;
    private final ShiftService shiftService;

    public AdminDashboardController(TokenQueueService tokenQueueService,
                                    HomeVisitService homeVisitService,
                                    NotificationService notificationService,
                                    ContactService contactService,
                                    ClinicInfoService clinicInfoService,
                                    ShiftService shiftService) {
        this.tokenQueueService = tokenQueueService;
        this.homeVisitService = homeVisitService;
        this.notificationService = notificationService;
        this.contactService = contactService;
        this.clinicInfoService = clinicInfoService;
        this.shiftService = shiftService;
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DashboardStatsDto>> getDashboardStats() {
        QueueStatusDto queueStatus = tokenQueueService.getLiveQueueStatus();
        ClinicInfo clinicInfo = clinicInfoService.getClinicInfo();
        List<Shift> activeShifts = shiftService.getActiveShifts();

        DashboardStatsDto stats = new DashboardStatsDto();
        stats.setTodayTotalTokens(queueStatus.getTotalTokensToday());
        stats.setCurrentTokenNumber(queueStatus.getCurrentTokenNumber());
        stats.setCurrentTokenPatientName(queueStatus.getCurrentPatientMaskedName());
        stats.setWaitingPatientsCount(queueStatus.getWaitingCount());
        stats.setCompletedPatientsCount(queueStatus.getCompletedCount());
        stats.setSkippedPatientsCount(queueStatus.getSkippedCount());

        stats.setPendingHomeVisitsCount((int) homeVisitService.getPendingCount());
        stats.setAcceptedHomeVisitsTodayCount((int) homeVisitService.getAcceptedTodayCount());
        stats.setTotalHomeVisits(homeVisitService.getAllHomeVisits(null, null).size());

        stats.setUnreadNotificationsCount((int) notificationService.getUnreadCount());
        stats.setNewContactInquiriesCount((int) contactService.getNewCount());

        stats.setClinicOperational(clinicInfo.isOperational());
        stats.setOperationalStatusMessage(clinicInfo.getOperationalStatusMessage());
        stats.setActiveShifts(activeShifts);

        return ResponseEntity.ok(ApiResponse.ok("Dashboard metrics retrieved", stats));
    }
}