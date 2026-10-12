package com.laxmiclinic.repository;

import com.laxmiclinic.model.ClinicInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClinicInfoRepository extends MongoRepository<ClinicInfo, String> {
    Optional<ClinicInfo> findFirstByOrderByIdAsc();
}
