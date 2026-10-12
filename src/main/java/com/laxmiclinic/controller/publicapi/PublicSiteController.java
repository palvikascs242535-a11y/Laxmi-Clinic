package com.laxmiclinic.controller.publicapi;

import com.laxmiclinic.dto.response.ApiResponse;
import com.laxmiclinic.model.ClinicInfo;
import com.laxmiclinic.model.Shift;
import com.laxmiclinic.service.ClinicInfoService;
import com.laxmiclinic.service.ShiftService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public")
public class PublicSiteController {

    private final ClinicInfoService clinicInfoService;
    private final ShiftService shiftService;

    public PublicSiteController(ClinicInfoService clinicInfoService, ShiftService shiftService) {
        this.clinicInfoService = clinicInfoService;
        this.shiftService = shiftService;
    }

    @GetMapping("/site-info")
    public ResponseEntity<ApiResponse<ClinicInfo>> getSiteInfo() {
        ClinicInfo info = clinicInfoService.getClinicInfo();
        return ResponseEntity.ok(ApiResponse.ok("Clinic site information retrieved", info));
    }

    @GetMapping("/shifts")
    public ResponseEntity<ApiResponse<List<Shift>>> getActiveShifts() {
        List<Shift> shifts = shiftService.getAllShifts();
        return ResponseEntity.ok(ApiResponse.ok("Clinic shifts retrieved", shifts));
    }
}
