package com.laxmiclinic.controller.adminapi;

import com.laxmiclinic.dto.response.ApiResponse;
import com.laxmiclinic.dto.response.TokenResponseDto;
import com.laxmiclinic.model.Token;
import com.laxmiclinic.service.TokenQueueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/tokens")
public class AdminTokenController {

    private final TokenQueueService tokenQueueService;

    public AdminTokenController(TokenQueueService tokenQueueService) {
        this.tokenQueueService = tokenQueueService;
    }

    @GetMapping("/today")
    public ResponseEntity<ApiResponse<List<TokenResponseDto>>> getTodayTokens(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) Token.TokenStatus status) {
        List<TokenResponseDto> tokens = tokenQueueService.getTokensForDate(date, status);
        return ResponseEntity.ok(ApiResponse.ok("Tokens list retrieved", tokens));
    }

    @PostMapping("/call-next")
    public ResponseEntity<ApiResponse<TokenResponseDto>> callNextToken() {
        TokenResponseDto token = tokenQueueService.callNextToken();
        return ResponseEntity.ok(ApiResponse.ok("Token #" + token.getTokenNumber() + " is now CALLED.", token));
    }

    @PostMapping("/{id}/call")
    public ResponseEntity<ApiResponse<TokenResponseDto>> callSpecificToken(@PathVariable String id) {
        TokenResponseDto token = tokenQueueService.callSpecificToken(id);
        return ResponseEntity.ok(ApiResponse.ok("Token #" + token.getTokenNumber() + " is now CALLED.", token));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<TokenResponseDto>> markComplete(
            @PathVariable String id,
            @RequestBody(required = false) Map<String, String> body) {
        String remark = (body != null) ? body.get("remark") : null;
        TokenResponseDto token = tokenQueueService.markTokenComplete(id, remark);
        return ResponseEntity.ok(ApiResponse.ok("Token #" + token.getTokenNumber() + " marked as COMPLETED.", token));
    }

    @PostMapping("/{id}/skip")
    public ResponseEntity<ApiResponse<TokenResponseDto>> skipToken(
            @PathVariable String id,
            @RequestBody(required = false) Map<String, String> body) {
        String remark = (body != null) ? body.get("remark") : null;
        TokenResponseDto token = tokenQueueService.skipToken(id, remark);
        return ResponseEntity.ok(ApiResponse.ok("Token #" + token.getTokenNumber() + " marked as SKIPPED.", token));
    }

    @PostMapping("/{id}/recall")
    public ResponseEntity<ApiResponse<TokenResponseDto>> recallToken(@PathVariable String id) {
        TokenResponseDto token = tokenQueueService.recallToken(id);
        return ResponseEntity.ok(ApiResponse.ok("Token #" + token.getTokenNumber() + " recalled back to WAITING queue.", token));
    }
}
