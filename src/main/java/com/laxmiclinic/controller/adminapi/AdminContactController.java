package com.laxmiclinic.controller.adminapi;

import com.laxmiclinic.dto.response.ApiResponse;
import com.laxmiclinic.model.ContactSubmission;
import com.laxmiclinic.service.ContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/contact-submissions")
public class AdminContactController {

    private final ContactService contactService;

    public AdminContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ContactSubmission>>> getAllSubmissions(
            @RequestParam(required = false) ContactSubmission.InquiryStatus status) {
        List<ContactSubmission> submissions = contactService.getAllSubmissions(status);
        return ResponseEntity.ok(ApiResponse.ok("Contact submissions retrieved", submissions));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<ContactSubmission>> updateStatus(
            @PathVariable String id,
            @RequestBody Map<String, String> payload) {
        String statusStr = payload.get("status");
        if (statusStr == null || statusStr.isBlank()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Status is required"));
        }

        ContactSubmission.InquiryStatus status = ContactSubmission.InquiryStatus.valueOf(statusStr.toUpperCase());
        String reply = payload.get("adminReply");

        ContactSubmission updated = contactService.updateSubmissionStatus(id, status, reply);
        return ResponseEntity.ok(ApiResponse.ok("Submission status updated", updated));
    }
}
