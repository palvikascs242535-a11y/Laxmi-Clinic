package com.laxmiclinic.service;

import com.laxmiclinic.dto.request.TokenGenerateRequest;
import com.laxmiclinic.dto.response.QueueStatusDto;
import com.laxmiclinic.dto.response.TokenResponseDto;
import com.laxmiclinic.exception.BadRequestException;
import com.laxmiclinic.exception.ResourceNotFoundException;
import com.laxmiclinic.model.ClinicInfo;
import com.laxmiclinic.model.Notification;
import com.laxmiclinic.model.Shift;
import com.laxmiclinic.model.Token;
import com.laxmiclinic.repository.TokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TokenQueueService {

    private static final Logger logger = LoggerFactory.getLogger(TokenQueueService.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final TokenRepository tokenRepository;
    private final ShiftService shiftService;
    private final ClinicInfoService clinicInfoService;
    private final NotificationService notificationService;

    public TokenQueueService(TokenRepository tokenRepository,
                             ShiftService shiftService,
                             ClinicInfoService clinicInfoService,
                             NotificationService notificationService) {
        this.tokenRepository = tokenRepository;
        this.shiftService = shiftService;
        this.clinicInfoService = clinicInfoService;
        this.notificationService = notificationService;
    }

    public String getTodayDateString() {
        return LocalDate.now().format(DATE_FORMATTER);
    }

    /**
     * Generates a new token for today in sequential order starting from 1.
     */
    public synchronized TokenResponseDto generateToken(TokenGenerateRequest request) {
        ClinicInfo clinicInfo = clinicInfoService.getClinicInfo();
        if (!clinicInfo.isOperational()) {
            throw new BadRequestException("Clinic is currently closed: " + clinicInfo.getOperationalStatusMessage());
        }

        String today = getTodayDateString();

        // Determine shift
        int shiftNum = 1;
        String shiftName = "General Consultation";
        if (request.getShiftNumber() != null && request.getShiftNumber() >= 1 && request.getShiftNumber() <= 5) {
            Shift shift = shiftService.getShiftByNumber(request.getShiftNumber());
            if (!shift.isActive()) {
                throw new BadRequestException("Selected shift '" + shift.getName() + "' is currently INACTIVE / CLOSED. Please select an active shift.");
            }
            shiftNum = shift.getShiftNumber();
            shiftName = shift.getName();
        } else {
            List<Shift> activeShifts = shiftService.getActiveShifts();
            if (!activeShifts.isEmpty()) {
                Shift first = activeShifts.get(0);
                shiftNum = first.getShiftNumber();
                shiftName = first.getName();
            }
        }

        // Calculate next sequential token number for today
        int nextTokenNumber = 1;
        Optional<Token> lastTokenToday = tokenRepository.findFirstByTokenDateOrderByTokenNumberDesc(today);
        if (lastTokenToday.isPresent()) {
            nextTokenNumber = lastTokenToday.get().getTokenNumber() + 1;
        }

        Token token = new Token(
                nextTokenNumber,
                today,
                request.getPatientName().trim(),
                request.getPatientPhone().trim(),
                request.getAge(),
                request.getGender(),
                request.getSymptoms(),
                shiftNum,
                shiftName
        );

        Token saved = tokenRepository.save(token);
        logger.info("Generated Token #{} for patient {} on {}", saved.getTokenNumber(), saved.getPatientName(), today);

        // Notify Admin of new queue entry
        notificationService.createNotification(
                "New Token #" + saved.getTokenNumber(),
                "Patient " + saved.getPatientName() + " generated Token #" + saved.getTokenNumber() + " (" + saved.getShiftName() + ")",
                Notification.NotificationType.TOKEN_ALERT,
                Notification.NotificationPriority.NORMAL,
                saved.getId(),
                "/admin.html#queue"
        );

        return mapToDto(saved);
    }

    /**
     * Gets live queue status for today.
     */
    public QueueStatusDto getLiveQueueStatus() {
        String today = getTodayDateString();
        ClinicInfo clinicInfo = clinicInfoService.getClinicInfo();

        QueueStatusDto dto = new QueueStatusDto();
        dto.setTodayDate(today);
        dto.setClinicOperational(clinicInfo.isOperational());
        dto.setOperationalMessage(clinicInfo.getOperationalStatusMessage());
        dto.setNoticeBanner(clinicInfo.getNoticeBanner());

        // Find currently CALLED token
        Optional<Token> calledToken = tokenRepository.findFirstByTokenDateAndStatusOrderByCalledAtDesc(today, Token.TokenStatus.CALLED);
        if (calledToken.isPresent()) {
            Token cur = calledToken.get();
            dto.setCurrentTokenNumber(cur.getTokenNumber());
            dto.setCurrentTokenStatus(cur.getStatus().name());
            dto.setCurrentPatientMaskedName(maskPatientName(cur.getPatientName()));
            dto.setCurrentShiftName(cur.getShiftName());
        } else {
            dto.setCurrentTokenNumber(0);
            dto.setCurrentTokenStatus("NO_ACTIVE_CALL");
            dto.setCurrentPatientMaskedName("None");
            dto.setCurrentShiftName("N/A");
        }

        dto.setWaitingCount((int) tokenRepository.countByTokenDateAndStatus(today, Token.TokenStatus.WAITING));
        dto.setCalledCount((int) tokenRepository.countByTokenDateAndStatus(today, Token.TokenStatus.CALLED));
        dto.setCompletedCount((int) tokenRepository.countByTokenDateAndStatus(today, Token.TokenStatus.COMPLETED));
        dto.setSkippedCount((int) tokenRepository.countByTokenDateAndStatus(today, Token.TokenStatus.SKIPPED));
        dto.setTotalTokensToday((int) tokenRepository.countByTokenDate(today));

        // Get 10 recent tokens
        List<Token> recent = tokenRepository.findByTokenDateOrderByTokenNumberAsc(today);
        dto.setRecentTokens(recent.stream().limit(15).map(this::mapToDto).collect(Collectors.toList()));

        return dto;
    }

    /**
     * Checks status for a given token number and date.
     */
    public TokenResponseDto checkTokenStatus(String tokenDate, int tokenNumber) {
        String date = (tokenDate != null && !tokenDate.isBlank()) ? tokenDate : getTodayDateString();
        Token token = tokenRepository.findByTokenDateAndTokenNumber(date, tokenNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found for Date: " + date + " Number: #" + tokenNumber));

        return mapToDto(token);
    }

    /**
     * Patient search for their tokens by mobile number today.
     */
    public List<TokenResponseDto> getTokensByPhone(String phone, String tokenDate) {
        String date = (tokenDate != null && !tokenDate.isBlank()) ? tokenDate : getTodayDateString();
        List<Token> tokens = tokenRepository.findByTokenDateAndPatientPhoneOrderByTokenNumberDesc(date, phone.trim());
        return tokens.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    /**
     * Admin: Advances queue to next waiting token.
     * Completes currently CALLED token if any, then marks next WAITING token as CALLED.
     */
    public synchronized TokenResponseDto callNextToken() {
        String today = getTodayDateString();

        // 1. If there's an existing CALLED token, complete it automatically
        Optional<Token> currentCalled = tokenRepository.findFirstByTokenDateAndStatusOrderByCalledAtDesc(today, Token.TokenStatus.CALLED);
        currentCalled.ifPresent(token -> {
            token.setStatus(Token.TokenStatus.COMPLETED);
            token.setCompletedAt(LocalDateTime.now());
            tokenRepository.save(token);
            logger.info("Auto-completed Token #{} before calling next", token.getTokenNumber());
        });

        // 2. Find the lowest WAITING token
        Optional<Token> nextWaiting = tokenRepository.findFirstByTokenDateAndStatusOrderByTokenNumberAsc(today, Token.TokenStatus.WAITING);
        if (nextWaiting.isEmpty()) {
            throw new BadRequestException("No waiting patients in queue for today.");
        }

        Token tokenToCall = nextWaiting.get();
        tokenToCall.setStatus(Token.TokenStatus.CALLED);
        tokenToCall.setCalledAt(LocalDateTime.now());
        Token saved = tokenRepository.save(tokenToCall);

        notificationService.createNotification(
                "Calling Token #" + saved.getTokenNumber(),
                "Token #" + saved.getTokenNumber() + " (" + saved.getPatientName() + ") is now CALLED to the doctor's room.",
                Notification.NotificationType.TOKEN_ALERT,
                Notification.NotificationPriority.HIGH,
                saved.getId(),
                "/admin.html#queue"
        );

        return mapToDto(saved);
    }

    /**
     * Admin: Calls a specific token directly.
     */
    public synchronized TokenResponseDto callSpecificToken(String tokenId) {
        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found with ID: " + tokenId));

        // Complete any other currently CALLED token for today
        Optional<Token> currentCalled = tokenRepository.findFirstByTokenDateAndStatusOrderByCalledAtDesc(token.getTokenDate(), Token.TokenStatus.CALLED);
        currentCalled.ifPresent(c -> {
            if (!c.getId().equals(token.getId())) {
                c.setStatus(Token.TokenStatus.COMPLETED);
                c.setCompletedAt(LocalDateTime.now());
                tokenRepository.save(c);
            }
        });

        token.setStatus(Token.TokenStatus.CALLED);
        token.setCalledAt(LocalDateTime.now());
        Token saved = tokenRepository.save(token);

        notificationService.createNotification(
                "Calling Token #" + saved.getTokenNumber(),
                "Token #" + saved.getTokenNumber() + " (" + saved.getPatientName() + ") is now CALLED.",
                Notification.NotificationType.TOKEN_ALERT,
                Notification.NotificationPriority.HIGH,
                saved.getId(),
                "/admin.html#queue"
        );

        return mapToDto(saved);
    }

    /**
     * Admin: Marks token as COMPLETED.
     */
    public TokenResponseDto markTokenComplete(String tokenId, String remark) {
        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found with ID: " + tokenId));

        token.setStatus(Token.TokenStatus.COMPLETED);
        token.setCompletedAt(LocalDateTime.now());
        if (remark != null) token.setAdminRemark(remark);
        Token saved = tokenRepository.save(token);

        return mapToDto(saved);
    }

    /**
     * Admin: Skips token.
     */
    public TokenResponseDto skipToken(String tokenId, String remark) {
        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found with ID: " + tokenId));

        token.setStatus(Token.TokenStatus.SKIPPED);
        token.setSkippedAt(LocalDateTime.now());
        if (remark != null) token.setAdminRemark(remark);
        Token saved = tokenRepository.save(token);

        return mapToDto(saved);
    }

    /**
     * Admin: Recalls skipped or completed token back to WAITING.
     */
    public TokenResponseDto recallToken(String tokenId) {
        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found with ID: " + tokenId));

        token.setStatus(Token.TokenStatus.WAITING);
        token.setSkippedAt(null);
        Token saved = tokenRepository.save(token);

        return mapToDto(saved);
    }

    /**
     * Admin: Get all tokens for a date with optional status filter.
     */
    public List<TokenResponseDto> getTokensForDate(String date, Token.TokenStatus status) {
        String queryDate = (date != null && !date.isBlank()) ? date : getTodayDateString();
        List<Token> tokens;
        if (status != null) {
            tokens = tokenRepository.findByTokenDateAndStatusOrderByTokenNumberAsc(queryDate, status);
        } else {
            tokens = tokenRepository.findByTokenDateOrderByTokenNumberAsc(queryDate);
        }
        return tokens.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    /**
     * Helper to map Token entity to TokenResponseDto with calculated queue position.
     */
    public TokenResponseDto mapToDto(Token token) {
        TokenResponseDto dto = new TokenResponseDto();
        dto.setId(token.getId());
        dto.setTokenNumber(token.getTokenNumber());
        dto.setTokenDate(token.getTokenDate());
        dto.setPatientName(token.getPatientName());
        dto.setPatientPhone(token.getPatientPhone());
        dto.setAge(token.getAge());
        dto.setGender(token.getGender());
        dto.setSymptoms(token.getSymptoms());
        dto.setShiftNumber(token.getShiftNumber());
        dto.setShiftName(token.getShiftName());
        dto.setStatus(token.getStatus());
        dto.setCreatedAt(token.getCreatedAt());
        dto.setCalledAt(token.getCalledAt());
        dto.setCompletedAt(token.getCompletedAt());

        // Find current token number
        Optional<Token> called = tokenRepository.findFirstByTokenDateAndStatusOrderByCalledAtDesc(token.getTokenDate(), Token.TokenStatus.CALLED);
        dto.setCurrentTokenNumber(called.map(Token::getTokenNumber).orElse(0));

        // Calculate patients ahead if in WAITING status
        if (token.getStatus() == Token.TokenStatus.WAITING) {
            long countAhead = tokenRepository.countByTokenDateAndTokenNumberLessThanAndStatus(
                    token.getTokenDate(),
                    token.getTokenNumber(),
                    Token.TokenStatus.WAITING
            );
            dto.setPatientsAhead((int) countAhead);
            dto.setEstimatedWaitMinutes((int) countAhead * 10);
            dto.setStatusMessage("Waiting in queue. " + countAhead + " patients ahead of you.");
        } else if (token.getStatus() == Token.TokenStatus.CALLED) {
            dto.setPatientsAhead(0);
            dto.setEstimatedWaitMinutes(0);
            dto.setStatusMessage("It is your turn! Please proceed to the Doctor's consultation room.");
        } else if (token.getStatus() == Token.TokenStatus.COMPLETED) {
            dto.setPatientsAhead(0);
            dto.setEstimatedWaitMinutes(0);
            dto.setStatusMessage("Consultation completed. Thank you for visiting Laxmi Clinic!");
        } else if (token.getStatus() == Token.TokenStatus.SKIPPED) {
            dto.setPatientsAhead(0);
            dto.setEstimatedWaitMinutes(0);
            dto.setStatusMessage("Token was skipped. Please speak to the clinic receptionist to recall.");
        } else {
            dto.setPatientsAhead(0);
            dto.setEstimatedWaitMinutes(0);
            dto.setStatusMessage("Token status: " + token.getStatus());
        }

        return dto;
    }

    private String maskPatientName(String name) {
        if (name == null || name.isBlank()) return "Anonymous";
        String[] parts = name.trim().split("\\s+");
        if (parts.length == 1) {
            if (parts[0].length() <= 2) return parts[0];
            return parts[0].substring(0, 2) + "***";
        }
        return parts[0] + " " + parts[parts.length - 1].charAt(0) + "***";
    }
}
