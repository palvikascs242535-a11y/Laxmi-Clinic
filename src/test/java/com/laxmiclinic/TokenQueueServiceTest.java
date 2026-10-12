package com.laxmiclinic;

import com.laxmiclinic.dto.request.TokenGenerateRequest;
import com.laxmiclinic.dto.response.TokenResponseDto;
import com.laxmiclinic.model.ClinicInfo;
import com.laxmiclinic.model.Shift;
import com.laxmiclinic.model.Token;
import com.laxmiclinic.repository.TokenRepository;
import com.laxmiclinic.service.ClinicInfoService;
import com.laxmiclinic.service.NotificationService;
import com.laxmiclinic.service.ShiftService;
import com.laxmiclinic.service.TokenQueueService;
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
class TokenQueueServiceTest {

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private ShiftService shiftService;

    @Mock
    private ClinicInfoService clinicInfoService;

    @Mock
    private NotificationService notificationService;

    private TokenQueueService tokenQueueService;

    @BeforeEach
    void setUp() {
        tokenQueueService = new TokenQueueService(tokenRepository, shiftService, clinicInfoService, notificationService);
    }

    @Test
    void testGenerateFirstTokenOfDay() {
        ClinicInfo clinicInfo = new ClinicInfo();
        clinicInfo.setOperational(true);
        when(clinicInfoService.getClinicInfo()).thenReturn(clinicInfo);

        Shift shift = new Shift(1, "Morning Shift", "09:00 AM", "11:30 AM", "Morning", true);
        when(shiftService.getActiveShifts()).thenReturn(List.of(shift));

        // No tokens exist today yet
        when(tokenRepository.findFirstByTokenDateOrderByTokenNumberDesc(any())).thenReturn(Optional.empty());
        when(tokenRepository.save(any(Token.class))).thenAnswer(invocation -> {
            Token t = invocation.getArgument(0);
            t.setId("token-id-1");
            return t;
        });

        TokenGenerateRequest req = new TokenGenerateRequest();
        req.setPatientName("Aarav Patel");
        req.setPatientPhone("9876543210");
        req.setAge(28);
        req.setGender("Male");

        TokenResponseDto response = tokenQueueService.generateToken(req);

        assertNotNull(response);
        assertEquals(1, response.getTokenNumber());
        assertEquals("Aarav Patel", response.getPatientName());
        assertEquals(Token.TokenStatus.WAITING, response.getStatus());
        verify(tokenRepository, times(1)).save(any(Token.class));
    }

    @Test
    void testSequentialTokenIncrement() {
        ClinicInfo clinicInfo = new ClinicInfo();
        clinicInfo.setOperational(true);
        when(clinicInfoService.getClinicInfo()).thenReturn(clinicInfo);

        Shift shift = new Shift(1, "Morning Shift", "09:00 AM", "11:30 AM", "Morning", true);
        when(shiftService.getActiveShifts()).thenReturn(List.of(shift));

        // Previous token was #4
        Token prevToken = new Token();
        prevToken.setTokenNumber(4);
        when(tokenRepository.findFirstByTokenDateOrderByTokenNumberDesc(any())).thenReturn(Optional.of(prevToken));
        when(tokenRepository.save(any(Token.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TokenGenerateRequest req = new TokenGenerateRequest();
        req.setPatientName("Priya Sharma");
        req.setPatientPhone("9876543211");

        TokenResponseDto response = tokenQueueService.generateToken(req);

        assertEquals(5, response.getTokenNumber());
    }

    @Test
    void testCallNextTokenAdvancesQueue() {
        Token waitingToken = new Token(1, tokenQueueService.getTodayDateString(), "Patient One", "9876543210", 30, "Female", "Cold", 1, "Morning Shift");
        waitingToken.setId("token-1");
        waitingToken.setStatus(Token.TokenStatus.WAITING);

        when(tokenRepository.findFirstByTokenDateAndStatusOrderByCalledAtDesc(any(), eq(Token.TokenStatus.CALLED)))
                .thenReturn(Optional.empty());
        when(tokenRepository.findFirstByTokenDateAndStatusOrderByTokenNumberAsc(any(), eq(Token.TokenStatus.WAITING)))
                .thenReturn(Optional.of(waitingToken));
        when(tokenRepository.save(any(Token.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TokenResponseDto called = tokenQueueService.callNextToken();

        assertEquals(Token.TokenStatus.CALLED, called.getStatus());
        assertEquals("Patient One", called.getPatientName());
        verify(tokenRepository, times(1)).save(waitingToken);
    }
}
