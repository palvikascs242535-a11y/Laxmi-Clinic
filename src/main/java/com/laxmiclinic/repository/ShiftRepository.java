package com.laxmiclinic.repository;

import com.laxmiclinic.model.Shift;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShiftRepository extends MongoRepository<Shift, String> {
    List<Shift> findAllByOrderByShiftNumberAsc();
    List<Shift> findByActiveTrueOrderByShiftNumberAsc();
    Optional<Shift> findByShiftNumber(int shiftNumber);
    long count();
}
