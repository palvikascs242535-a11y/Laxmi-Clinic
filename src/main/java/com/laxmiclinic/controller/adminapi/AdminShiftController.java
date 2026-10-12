package com.laxmiclinic.controller.adminapi;

import com.laxmiclinic.dto.request.ShiftRequest;
import com.laxmiclinic.dto.response.ApiResponse;
import com.laxmiclinic.model.Shift;
import com.laxmiclinic.service.ShiftService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/shifts")
public class AdminShiftController {

    private final ShiftService shiftService;

    public AdminShiftController(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Shift>>> getAllShifts() {
        List<Shift> shifts = shiftService.getAllShifts();
        return ResponseEntity.ok(ApiResponse.ok("All clinic shifts retrieved", shifts));
    }

    @PutMapping("/{shiftNumber}")
    public ResponseEntity<ApiResponse<Shift>> updateShift(
            @PathVariable int shiftNumber,
            @Valid @RequestBody ShiftRequest request) {
        Shift updated = shiftService.updateShift(shiftNumber, request);
        return ResponseEntity.ok(ApiResponse.ok("Shift #" + shiftNumber + " updated successfully", updated));
    }

    @PutMapping("/{shiftNumber}/toggle")
    public ResponseEntity<ApiResponse<Shift>> toggleShiftActive(
            @PathVariable int shiftNumber,
            @RequestBody Map<String, Boolean> payload) {
        boolean active = Boolean.TRUE.equals(payload.get("active"));
        Shift updated = shiftService.toggleShiftActive(shiftNumber, active);
        return ResponseEntity.ok(ApiResponse.ok("Shift #" + shiftNumber + " active status set to " + active, updated));
    }

    @PostMapping("/reset-defaults")
    public ResponseEntity<ApiResponse<List<Shift>>> resetDefaultShifts() {
        List<Shift> shifts = shiftService.initDefault5Shifts();
        return ResponseEntity.ok(ApiResponse.ok("Reset shifts to default 2 configuration", shifts));
    }
}
