package com.hanniel.ticketBookingSystem.services.auth;

import com.hanniel.ticketBookingSystem.config.security.TokenService;
import com.hanniel.ticketBookingSystem.domain.user.User;
import com.hanniel.ticketBookingSystem.domain.user.UserRole;
import com.hanniel.ticketBookingSystem.dtos.auth.AuthenticationDTO;
import com.hanniel.ticketBookingSystem.dtos.auth.LoginResponseDTO;
import com.hanniel.ticketBookingSystem.dtos.auth.RegisterDTO;
import com.hanniel.ticketBookingSystem.exceptions.auth.InvalidCredentialsException;
import com.hanniel.ticketBookingSystem.exceptions.auth.UserAlreadyExistsException;
import com.hanniel.ticketBookingSystem.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public void register(RegisterDTO data) {
        log.info("Registering new user with email: {}", data.email());

        if (userRepository.findByEmail(data.email()).isPresent()) {
            log.error("Registration failed. Email already registered: {}", data.email());
            throw new UserAlreadyExistsException("Email is already registered");
        }

        String encryptedPassword = passwordEncoder.encode(data.password());
        UserRole userRole = data.role() != null ? data.role() : UserRole.USER;

        User newUser = User.builder()
                .email(data.email())
                .password(encryptedPassword)
                .role(userRole)
                .build();

        userRepository.save(newUser);
        log.info("User registered successfully: {}", data.email());
    }

    public LoginResponseDTO login(AuthenticationDTO data) {
        log.info("Authenticating user with email: {}", data.email());
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
            var auth = this.authenticationManager.authenticate(usernamePassword);

            var token = tokenService.generateToken((User) auth.getPrincipal());
            log.info("User logged in successfully: {}", data.email());
            return new LoginResponseDTO(token);
        } catch (BadCredentialsException e) {
            log.error("Authentication failed for user: {}. Bad credentials.", data.email());
            throw new InvalidCredentialsException("Invalid email or password");
        }
    }
}
