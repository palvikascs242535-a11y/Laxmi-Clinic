package com.laxmiclinic.service;

import com.laxmiclinic.dto.request.ChangePasswordRequest;
import com.laxmiclinic.dto.request.LoginRequest;
import com.laxmiclinic.dto.response.AuthResponse;
import com.laxmiclinic.exception.BadRequestException;
import com.laxmiclinic.model.AdminUser;
import com.laxmiclinic.repository.AdminUserRepository;
import com.laxmiclinic.security.JwtTokenProvider;
import com.laxmiclinic.security.UserPrincipal;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager,
                       JwtTokenProvider tokenProvider,
                       AdminUserRepository adminUserRepository,
                       PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername().trim(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        // Update last login
        adminUserRepository.findByUsername(userPrincipal.getUsername()).ifPresent(user -> {
            user.setLastLoginAt(LocalDateTime.now());
            adminUserRepository.save(user);
        });

        String role = userPrincipal.getAuthorities().stream()
                .findFirst()
                .map(Object::toString)
                .orElse("ROLE_ADMIN");

        return new AuthResponse(
                jwt,
                userPrincipal.getUsername(),
                userPrincipal.getFullName(),
                userPrincipal.getEmail(),
                role,
                tokenProvider.getExpirationInMs()
        );
    }

    public void changePassword(String username, ChangePasswordRequest request) {
        AdminUser user = adminUserRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password does not match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        adminUserRepository.save(user);
    }

    public AdminUser getCurrentUser(String username) {
        return adminUserRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("User not found: " + username));
    }
}
