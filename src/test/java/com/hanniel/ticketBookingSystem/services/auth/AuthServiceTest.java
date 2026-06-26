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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_Success() {
        RegisterDTO dto = new RegisterDTO("new@example.com", "password123", UserRole.USER);
        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(dto.password())).thenReturn("encodedPassword");

        authService.register(dto);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_UserAlreadyExists_ThrowsException() {
        RegisterDTO dto = new RegisterDTO("existing@example.com", "password123", UserRole.USER);
        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistsException.class, () -> {
            authService.register(dto);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_Success() {
        AuthenticationDTO dto = new AuthenticationDTO("user@example.com", "password123");
        User user = User.builder().email(dto.email()).password("encodedPassword").build();

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(tokenService.generateToken(user)).thenReturn("mockedToken");

        LoginResponseDTO response = authService.login(dto);

        assertNotNull(response);
        assertEquals("mockedToken", response.token());
    }

    @Test
    void login_InvalidCredentials_ThrowsException() {
        AuthenticationDTO dto = new AuthenticationDTO("user@example.com", "wrongpassword");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(InvalidCredentialsException.class, () -> {
            authService.login(dto);
        });
    }
}
