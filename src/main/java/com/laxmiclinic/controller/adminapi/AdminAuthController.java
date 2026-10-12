package com.laxmiclinic.controller.adminapi;

import com.laxmiclinic.dto.request.ChangePasswordRequest;
import com.laxmiclinic.dto.response.ApiResponse;
import com.laxmiclinic.model.AdminUser;
import com.laxmiclinic.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/auth")
public class AdminAuthController {

    private final AuthService authService;

    public AdminAuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        AdminUser user = authService.getCurrentUser(userDetails.getUsername());
        Map<String, Object> data = new HashMap<>();
        data.put("id", user.getId());
        data.put("username", user.getUsername());
        data.put("fullName", user.getFullName());
        data.put("email", user.getEmail());
        data.put("role", user.getRole());
        data.put("lastLoginAt", user.getLastLoginAt());

        return ResponseEntity.ok(ApiResponse.ok("User profile retrieved", data));
    }

    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ChangePasswordRequest request) {
        authService.changePassword(userDetails.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.ok("Password changed successfully", null));
    }
}
