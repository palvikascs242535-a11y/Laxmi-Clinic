package com.laxmiclinic.service;

import com.laxmiclinic.dto.request.ContactRequest;
import com.laxmiclinic.exception.ResourceNotFoundException;
import com.laxmiclinic.model.ContactSubmission;
import com.laxmiclinic.model.Notification;
import com.laxmiclinic.repository.ContactSubmissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {

    private final ContactSubmissionRepository contactSubmissionRepository;
    private final NotificationService notificationService;

    public ContactService(ContactSubmissionRepository contactSubmissionRepository, NotificationService notificationService) {
        this.contactSubmissionRepository = contactSubmissionRepository;
        this.notificationService = notificationService;
    }

    public ContactSubmission submitContactForm(ContactRequest request) {
        ContactSubmission submission = new ContactSubmission(
                request.getName().trim(),
                request.getEmail() != null ? request.getEmail().trim() : null,
                request.getMobile().trim(),
                request.getSubject() != null ? request.getSubject().trim() : "General Inquiry",
                request.getMessage().trim()
        );

        ContactSubmission saved = contactSubmissionRepository.save(submission);

        notificationService.createNotification(
                "New Contact Inquiry",
                "Message received from " + saved.getName() + " (" + saved.getMobile() + "): " + saved.getSubject(),
                Notification.NotificationType.CONTACT_INQUIRY,
                Notification.NotificationPriority.NORMAL,
                saved.getId(),
                "/admin.html#inquiries"
        );

        return saved;
    }

    public List<ContactSubmission> getAllSubmissions(ContactSubmission.InquiryStatus status) {
        if (status != null) {
            return contactSubmissionRepository.findByStatusOrderByCreatedAtDesc(status);
        }
        return contactSubmissionRepository.findAllByOrderByCreatedAtDesc();
    }

    public ContactSubmission getSubmissionById(String id) {
        return contactSubmissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact inquiry not found with id: " + id));
    }

    public ContactSubmission updateSubmissionStatus(String id, ContactSubmission.InquiryStatus status, String reply) {
        ContactSubmission submission = getSubmissionById(id);
        submission.setStatus(status);
        if (reply != null) {
            submission.setAdminReply(reply);
        }
        return contactSubmissionRepository.save(submission);
    }

    public long getNewCount() {
        return contactSubmissionRepository.countByStatus(ContactSubmission.InquiryStatus.NEW);
    }
}
