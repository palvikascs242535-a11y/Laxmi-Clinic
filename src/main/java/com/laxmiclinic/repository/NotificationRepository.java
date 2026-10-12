package com.laxmiclinic.repository;

import com.laxmiclinic.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    
    List<Notification> findTop50ByOrderByCreatedAtDesc();
    
    List<Notification> findByIsReadFalseOrderByCreatedAtDesc();
    
    long countByIsReadFalse();
    
    List<Notification> findByTargetRoleOrderByCreatedAtDesc(String targetRole);
}
