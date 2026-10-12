package com.laxmiclinic.controller.publicapi;

import com.laxmiclinic.dto.request.LoginRequest;
import com.laxmiclinic.dto.response.ApiResponse;
import com.laxmiclinic.dto.response.AuthResponse;
import com.laxmiclinic.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse authResponse = authService.login(loginRequest);
        return ResponseEntity.ok(ApiResponse.ok("Login successful", authResponse));
    }
}
