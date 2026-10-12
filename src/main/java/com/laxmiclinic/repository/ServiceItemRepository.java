package com.laxmiclinic.repository;

import com.laxmiclinic.model.ServiceItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceItemRepository extends MongoRepository<ServiceItem, String> {
    List<ServiceItem> findAllByOrderByDisplayOrderAsc();
    List<ServiceItem> findByActiveTrueOrderByDisplayOrderAsc();
}
