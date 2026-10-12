package com.laxmiclinic.controller.publicapi;

import com.laxmiclinic.dto.request.TokenGenerateRequest;
import com.laxmiclinic.dto.response.ApiResponse;
import com.laxmiclinic.dto.response.QueueStatusDto;
import com.laxmiclinic.dto.response.TokenResponseDto;
import com.laxmiclinic.service.TokenQueueService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/tokens")
public class PublicTokenController {

    private final TokenQueueService tokenQueueService;

    public PublicTokenController(TokenQueueService tokenQueueService) {
        this.tokenQueueService = tokenQueueService;
    }

    /**
     * Generates a new online token for the patient.
     */
    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<TokenResponseDto>> generateToken(@Valid @RequestBody TokenGenerateRequest request) {
        TokenResponseDto response = tokenQueueService.generateToken(request);
        return new ResponseEntity<>(ApiResponse.ok("Token generated successfully! Please save your token number.", response), HttpStatus.CREATED);
    }

    /**
     * Gets live queue status and metrics for today.
     */
    @GetMapping("/live-status")
    public ResponseEntity<ApiResponse<QueueStatusDto>> getLiveQueueStatus() {
        QueueStatusDto status = tokenQueueService.getLiveQueueStatus();
        return ResponseEntity.ok(ApiResponse.ok("Live queue status retrieved", status));
    }

    /**
     * Checks individual token status by date and token number.
     */
    @GetMapping("/check")
    public ResponseEntity<ApiResponse<TokenResponseDto>> checkToken(
            @RequestParam(required = false) String date,
            @RequestParam int tokenNumber) {
        TokenResponseDto token = tokenQueueService.checkTokenStatus(date, tokenNumber);
        return ResponseEntity.ok(ApiResponse.ok("Token details retrieved", token));
    }

    /**
     * Looks up patient's tokens today by mobile number.
     */
    @GetMapping("/my-tokens")
    public ResponseEntity<ApiResponse<List<TokenResponseDto>>> getMyTokens(
            @RequestParam String phone,
            @RequestParam(required = false) String date) {
        List<TokenResponseDto> tokens = tokenQueueService.getTokensByPhone(phone, date);
        return ResponseEntity.ok(ApiResponse.ok("Tokens retrieved for phone: " + phone, tokens));
    }
}
