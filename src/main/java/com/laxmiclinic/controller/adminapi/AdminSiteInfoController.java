package com.laxmiclinic.controller.adminapi;

import com.laxmiclinic.dto.request.ClinicInfoRequest;
import com.laxmiclinic.dto.response.ApiResponse;
import com.laxmiclinic.model.ClinicInfo;
import com.laxmiclinic.service.ClinicInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/site-info")
public class AdminSiteInfoController {

    private final ClinicInfoService clinicInfoService;

    public AdminSiteInfoController(ClinicInfoService clinicInfoService) {
        this.clinicInfoService = clinicInfoService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<ClinicInfo>> getSiteInfo() {
        ClinicInfo info = clinicInfoService.getClinicInfo();
        return ResponseEntity.ok(ApiResponse.ok("Clinic site information retrieved", info));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<ClinicInfo>> updateSiteInfo(@RequestBody ClinicInfoRequest request) {
        ClinicInfo updated = clinicInfoService.updateClinicInfo(request);
        return ResponseEntity.ok(ApiResponse.ok("Clinic site information updated successfully", updated));
    }

    @PutMapping("/operational-status")
    public ResponseEntity<ApiResponse<ClinicInfo>> toggleOperationalStatus(@RequestBody Map<String, Object> payload) {
        boolean operational = Boolean.TRUE.equals(payload.get("operational"));
        String statusMessage = (String) payload.get("operationalStatusMessage");

        ClinicInfo updated = clinicInfoService.toggleOperationalStatus(operational, statusMessage);
        return ResponseEntity.ok(ApiResponse.ok("Operational status updated successfully", updated));
    }
}
