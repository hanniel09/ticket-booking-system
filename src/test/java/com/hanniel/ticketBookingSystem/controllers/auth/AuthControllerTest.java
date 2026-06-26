package com.hanniel.ticketBookingSystem.controllers.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanniel.ticketBookingSystem.config.security.SecurityFilter;
import com.hanniel.ticketBookingSystem.config.security.TokenService;
import com.hanniel.ticketBookingSystem.dtos.auth.AuthenticationDTO;
import com.hanniel.ticketBookingSystem.dtos.auth.LoginResponseDTO;
import com.hanniel.ticketBookingSystem.dtos.auth.RegisterDTO;
import com.hanniel.ticketBookingSystem.domain.user.UserRole;
import com.hanniel.ticketBookingSystem.repositories.user.UserRepository;
import com.hanniel.ticketBookingSystem.services.auth.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private SecurityFilter securityFilter;

    @Test
    void register_Success() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO("register@example.com", "password123", UserRole.USER);
        doNothing().when(authService).register(any(RegisterDTO.class));

        mockMvc.perform(post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isCreated());

        verify(authService, times(1)).register(any(RegisterDTO.class));
    }

    @Test
    void login_Success() throws Exception {
        AuthenticationDTO authDTO = new AuthenticationDTO("user@example.com", "password123");
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO("mocked-jwt-token");
        when(authService.login(any(AuthenticationDTO.class))).thenReturn(loginResponseDTO);

        mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"));

        verify(authService, times(1)).login(any(AuthenticationDTO.class));
    }

    @Test
    void register_InvalidPayload_ReturnsBadRequest() throws Exception {
        RegisterDTO invalidDTO = new RegisterDTO("invalid-email", "short", UserRole.USER);

        mockMvc.perform(post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).register(any(RegisterDTO.class));
    }
}
