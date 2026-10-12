package com.laxmiclinic.service;

import com.laxmiclinic.dto.request.ShiftRequest;
import com.laxmiclinic.exception.BadRequestException;
import com.laxmiclinic.exception.ResourceNotFoundException;
import com.laxmiclinic.model.Shift;
import com.laxmiclinic.repository.ShiftRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShiftService {

    private final ShiftRepository shiftRepository;

    public ShiftService(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }

    public List<Shift> getAllShifts() {
        List<Shift> shifts = shiftRepository.findAllByOrderByShiftNumberAsc();
        if (shifts.isEmpty()) {
            return initDefault5Shifts();
        }
        return shifts;
    }

    public List<Shift> getActiveShifts() {
        return shiftRepository.findByActiveTrueOrderByShiftNumberAsc();
    }

    public Shift getShiftByNumber(int shiftNumber) {
        return shiftRepository.findByShiftNumber(shiftNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Shift not found with number: " + shiftNumber));
    }

    public Shift updateShift(int shiftNumber, ShiftRequest request) {
        if (shiftNumber < 1 || shiftNumber > 5) {
            throw new BadRequestException("Shift number must be between 1 and 5");
        }

        Shift shift = shiftRepository.findByShiftNumber(shiftNumber)
                .orElseGet(() -> new Shift(shiftNumber, request.getName(), request.getStartTime(), request.getEndTime(), request.getDescription(), true));

        shift.setName(request.getName());
        shift.setStartTime(request.getStartTime());
        shift.setEndTime(request.getEndTime());
        shift.setDescription(request.getDescription());
        shift.setMaxTokensCapacity(request.getMaxTokensCapacity() > 0 ? request.getMaxTokensCapacity() : 30);
        shift.setActive(request.isActive());
        shift.setUpdatedAt(LocalDateTime.now());

        return shiftRepository.save(shift);
    }

    public Shift toggleShiftActive(int shiftNumber, boolean active) {
        Shift shift = getShiftByNumber(shiftNumber);
        shift.setActive(active);
        shift.setUpdatedAt(LocalDateTime.now());
        return shiftRepository.save(shift);
    }

    public List<Shift> initDefault5Shifts() {
        List<Shift> defaultShifts = new ArrayList<>();
        defaultShifts.add(new Shift(1, "Morning Shift", "12:00 PM", "05:00 PM", "Afternoon consultation session", true));
        defaultShifts.add(new Shift(2, "Evening Shift", "07:00 PM", "12:00 AM", "Evening & Night consultation session", true));

        shiftRepository.deleteAll();
        return shiftRepository.saveAll(defaultShifts);
    }
}