package com.laxmiclinic;

import com.laxmiclinic.dto.request.ShiftRequest;
import com.laxmiclinic.model.Shift;
import com.laxmiclinic.repository.ShiftRepository;
import com.laxmiclinic.service.ShiftService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShiftServiceTest {

    @Mock
    private ShiftRepository shiftRepository;

    private ShiftService shiftService;

    @BeforeEach
    void setUp() {
        shiftService = new ShiftService(shiftRepository);
    }

    @Test
    void testInitDefault5Shifts() {
        when(shiftRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        List<Shift> shifts = shiftService.initDefault5Shifts();

        assertEquals(2, shifts.size());
        assertEquals(1, shifts.get(0).getShiftNumber());
        assertEquals("Morning Shift", shifts.get(0).getName());
        assertEquals(2, shifts.get(1).getShiftNumber());
        assertEquals("Evening Shift", shifts.get(1).getName());
    }

    @Test
    void testUpdateShift() {
        Shift existing = new Shift(1, "Morning Shift", "12:00 PM", "05:00 PM", "Desc", true);
        when(shiftRepository.findByShiftNumber(1)).thenReturn(Optional.of(existing));
        when(shiftRepository.save(any(Shift.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ShiftRequest req = new ShiftRequest();
        req.setShiftNumber(1);
        req.setName("Morning Shift OPD");
        req.setStartTime("12:00 PM");
        req.setEndTime("05:00 PM");
        req.setActive(true);

        Shift updated = shiftService.updateShift(1, req);

        assertEquals("Morning Shift OPD", updated.getName());
        assertEquals("12:00 PM", updated.getStartTime());
        verify(shiftRepository, times(1)).save(existing);
    }
}