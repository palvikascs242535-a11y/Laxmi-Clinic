package com.laxmiclinic.repository;

import com.laxmiclinic.model.ContactSubmission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactSubmissionRepository extends MongoRepository<ContactSubmission, String> {
    List<ContactSubmission> findAllByOrderByCreatedAtDesc();
    List<ContactSubmission> findByStatusOrderByCreatedAtDesc(ContactSubmission.InquiryStatus status);
    long countByStatus(ContactSubmission.InquiryStatus status);
}
