package com.laxmiclinic;

import com.laxmiclinic.dto.request.HomeVisitBookingRequest;
import com.laxmiclinic.exception.BadRequestException;
import com.laxmiclinic.model.HomeVisit;
import com.laxmiclinic.repository.HomeVisitRepository;
import com.laxmiclinic.service.HomeVisitService;
import com.laxmiclinic.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HomeVisitServiceTest {

    @Mock
    private HomeVisitRepository homeVisitRepository;

    @Mock
    private NotificationService notificationService;

    private HomeVisitService homeVisitService;

    @BeforeEach
    void setUp() {
        homeVisitService = new HomeVisitService(homeVisitRepository, notificationService);
    }

    @Test
    void testSuccessfulHomeVisitBooking() {
        String futureDate = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String timeSlot = "09:30 AM - 11:30 AM (Morning)";

        when(homeVisitRepository.existsByPreferredDateAndPreferredTimeSlotAndStatus(
                eq(futureDate), eq(timeSlot), eq(HomeVisit.VisitStatus.ACCEPTED)))
                .thenReturn(false);

        when(homeVisitRepository.save(any(HomeVisit.class))).thenAnswer(invocation -> {
            HomeVisit hv = invocation.getArgument(0);
            hv.setId("hv-123");
            return hv;
        });

        HomeVisitBookingRequest request = new HomeVisitBookingRequest();
        request.setPatientName("Suresh Nair");
        request.setMobile("9876543210");
        request.setAddress("Flat 204, Rosewood Heights");
        request.setPincode("110001");
        request.setPreferredDate(futureDate);
        request.setPreferredTimeSlot(timeSlot);

        HomeVisit booked = homeVisitService.bookHomeVisit(request);

        assertNotNull(booked);
        assertEquals(HomeVisit.VisitStatus.PENDING, booked.getStatus());
        assertEquals("Suresh Nair", booked.getPatientName());
        verify(homeVisitRepository, times(1)).save(any(HomeVisit.class));
    }

    @Test
    void testDoubleBookingPrevention() {
        String futureDate = LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String timeSlot = "09:30 AM - 11:30 AM (Morning)";

        // Simulate slot already booked & accepted by another patient
        when(homeVisitRepository.existsByPreferredDateAndPreferredTimeSlotAndStatus(
                eq(futureDate), eq(timeSlot), eq(HomeVisit.VisitStatus.ACCEPTED)))
                .thenReturn(true);

        HomeVisitBookingRequest request = new HomeVisitBookingRequest();
        request.setPatientName("Kavita Singh");
        request.setMobile("9876543212");
        request.setAddress("House 12, Main Road");
        request.setPincode("110001");
        request.setPreferredDate(futureDate);
        request.setPreferredTimeSlot(timeSlot);

        assertThrows(BadRequestException.class, () -> homeVisitService.bookHomeVisit(request));
        verify(homeVisitRepository, never()).save(any(HomeVisit.class));
    }
}
