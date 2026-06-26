package com.hanniel.ticketBookingSystem.config.security;

import com.hanniel.ticketBookingSystem.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {

    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
        ReflectionTestUtils.setField(tokenService, "secret", "myTestSecretKey");
    }

    @Test
    void generateAndValidateToken_Success() {
        User user = User.builder().email("user@example.com").build();

        String token = tokenService.generateToken(user);
        assertNotNull(token);

        String email = tokenService.validateToken(token);
        assertEquals("user@example.com", email);
    }

    @Test
    void validateToken_InvalidToken_ReturnsNull() {
        String invalidToken = "invalid.jwt.token";
        String email = tokenService.validateToken(invalidToken);
        assertNull(email);
    }
}
