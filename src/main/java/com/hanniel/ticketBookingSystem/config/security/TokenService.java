package com.hanniel.ticketBookingSystem.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.hanniel.ticketBookingSystem.domain.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
public class TokenService {

    @Value("${token.secret}")
    private String secret;

    private static final String ISSUER = "ticket-booking-system";
    private static final long EXPIRATION_SECONDS = 7200; // 2 hours

    public String generateToken(User user) {
        log.info("Generating token for user: {}", user.getEmail());
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(user.getEmail())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
            log.info("Token generated successfully for user: {}", user.getEmail());
            return token;
        } catch (JWTCreationException exception) {
            log.error("Error while generating token for user: {}", user.getEmail(), exception);
            throw new RuntimeException("Error while generating token", exception);
        }
    }

    public String validateToken(String token) {
        log.info("Validating JWT token");
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String subject = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token)
                    .getSubject();
            log.info("JWT token is valid. Subject: {}", subject);
            return subject;
        } catch (JWTVerificationException exception) {
            log.error("Token verification failed: {}", exception.getMessage());
            return null;
        }
    }

    private Instant genExpirationDate() {
        return Instant.now().plusSeconds(EXPIRATION_SECONDS);
    }
}
