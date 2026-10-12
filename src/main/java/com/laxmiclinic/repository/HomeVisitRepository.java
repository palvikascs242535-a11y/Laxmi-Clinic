package com.laxmiclinic.repository;

import com.laxmiclinic.model.HomeVisit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface HomeVisitRepository extends MongoRepository<HomeVisit, String> {
    
    List<HomeVisit> findAllByOrderByCreatedAtDesc();
    
    List<HomeVisit> findByMobileOrderByCreatedAtDesc(String mobile);
    
    List<HomeVisit> findByPreferredDateOrderByCreatedAtAsc(String preferredDate);
    
    List<HomeVisit> findByStatusOrderByCreatedAtDesc(HomeVisit.VisitStatus status);
    
    List<HomeVisit> findByPreferredDateAndPreferredTimeSlotAndStatusIn(
            String preferredDate, 
            String preferredTimeSlot, 
            Collection<HomeVisit.VisitStatus> statuses
    );
    
    boolean existsByPreferredDateAndPreferredTimeSlotAndStatus(
            String preferredDate, 
            String preferredTimeSlot, 
            HomeVisit.VisitStatus status
    );

    long countByStatus(HomeVisit.VisitStatus status);
    
    long countByPreferredDateAndStatus(String preferredDate, HomeVisit.VisitStatus status);
}
