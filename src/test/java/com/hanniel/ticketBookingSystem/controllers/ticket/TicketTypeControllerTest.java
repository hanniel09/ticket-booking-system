package com.hanniel.ticketBookingSystem.controllers.ticket;

import com.hanniel.ticketBookingSystem.config.security.SecurityFilter;
import com.hanniel.ticketBookingSystem.config.security.TokenService;
import com.hanniel.ticketBookingSystem.dtos.ticket.TicketTypeRequestDTO;
import com.hanniel.ticketBookingSystem.dtos.ticket.TicketTypeResponseDTO;
import com.hanniel.ticketBookingSystem.repositories.user.UserRepository;
import com.hanniel.ticketBookingSystem.services.ticket.TicketTypeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TicketTypeController.class)
class TicketTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TicketTypeService ticketTypeService;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private SecurityFilter securityFilter;

    @Test
    void createTicketType_Success() throws Exception {
        UUID eventId = UUID.randomUUID();
        TicketTypeResponseDTO response = new TicketTypeResponseDTO(UUID.randomUUID(), eventId, "VIP", new BigDecimal("150.00"), 100L);

        when(ticketTypeService.createTicketType(any(TicketTypeRequestDTO.class))).thenReturn(response);

        String json = String.format("{\"eventId\":\"%s\",\"name\":\"VIP\",\"price\":150.00,\"quantityAvailable\":100}", eventId);

        mockMvc.perform(post("/tickets/types")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("VIP"));

        verify(ticketTypeService, times(1)).createTicketType(any(TicketTypeRequestDTO.class));
    }

    @Test
    void getAllTicketTypes_Success() throws Exception {
        TicketTypeResponseDTO response = new TicketTypeResponseDTO(UUID.randomUUID(), UUID.randomUUID(), "VIP", new BigDecimal("150.00"), 100L);
        when(ticketTypeService.getAllTicketTypes()).thenReturn(List.of(response));

        mockMvc.perform(get("/tickets/types")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("VIP"));

        verify(ticketTypeService, times(1)).getAllTicketTypes();
    }
}
