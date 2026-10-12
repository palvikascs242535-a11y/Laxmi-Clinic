package com.laxmiclinic.controller.publicapi;

import com.laxmiclinic.dto.request.ContactRequest;
import com.laxmiclinic.dto.response.ApiResponse;
import com.laxmiclinic.model.ContactSubmission;
import com.laxmiclinic.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/contact")
public class PublicContactController {

    private final ContactService contactService;

    public PublicContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ContactSubmission>> submitContactForm(@Valid @RequestBody ContactRequest request) {
        ContactSubmission saved = contactService.submitContactForm(request);
        return new ResponseEntity<>(ApiResponse.ok("Thank you! Your message has been received. Our clinic team will reach out soon.", saved), HttpStatus.CREATED);
    }
}
