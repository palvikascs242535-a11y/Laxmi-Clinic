package com.laxmiclinic;

import com.laxmiclinic.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        String testSecret = "LaxmiClinicSuperSecretKey2026WithSufficient256BitEntropyLengthForSecurity1234567890";
        long expirationMs = 3600000; // 1 hour
        jwtTokenProvider = new JwtTokenProvider(testSecret, expirationMs);
    }

    @Test
    void testGenerateAndValidateJwtToken() {
        String username = "admin";
        String token = jwtTokenProvider.generateTokenFromUsername(username);

        assertNotNull(token);
        assertTrue(jwtTokenProvider.validateToken(token));
        assertEquals(username, jwtTokenProvider.getUsernameFromJwt(token));
    }

    @Test
    void testInvalidTokenReturnsFalse() {
        assertFalse(jwtTokenProvider.validateToken("invalid.jwt.token"));
    }
}
