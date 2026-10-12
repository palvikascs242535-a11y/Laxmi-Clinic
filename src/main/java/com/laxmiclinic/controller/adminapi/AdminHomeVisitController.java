package com.laxmiclinic.controller.adminapi;

import com.laxmiclinic.dto.response.ApiResponse;
import com.laxmiclinic.model.HomeVisit;
import com.laxmiclinic.service.HomeVisitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/home-visits")
public class AdminHomeVisitController {

    private final HomeVisitService homeVisitService;

    public AdminHomeVisitController(HomeVisitService homeVisitService) {
        this.homeVisitService = homeVisitService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<HomeVisit>>> getAllHomeVisits(
            @RequestParam(required = false) HomeVisit.VisitStatus status,
            @RequestParam(required = false) String date) {
        List<HomeVisit> visits = homeVisitService.getAllHomeVisits(status, date);
        return ResponseEntity.ok(ApiResponse.ok("Home visit requests retrieved", visits));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HomeVisit>> getHomeVisitById(@PathVariable String id) {
        HomeVisit visit = homeVisitService.getHomeVisitById(id);
        return ResponseEntity.ok(ApiResponse.ok("Home visit details retrieved", visit));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<HomeVisit>> updateStatus(
            @PathVariable String id,
            @RequestBody Map<String, String> payload) {
        String statusStr = payload.get("status");
        if (statusStr == null || statusStr.isBlank()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Status field is required"));
        }

        HomeVisit.VisitStatus newStatus = HomeVisit.VisitStatus.valueOf(statusStr.toUpperCase());
        String adminRemark = payload.get("adminRemark");
        String doctorNotes = payload.get("doctorNotes");

        HomeVisit updated = homeVisitService.updateVisitStatus(id, newStatus, adminRemark, doctorNotes);
        return ResponseEntity.ok(ApiResponse.ok("Home visit status updated to " + newStatus, updated));
    }
}
