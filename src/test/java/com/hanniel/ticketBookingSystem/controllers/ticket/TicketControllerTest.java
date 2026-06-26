package com.hanniel.ticketBookingSystem.controllers.ticket;

import com.hanniel.ticketBookingSystem.config.security.SecurityFilter;
import com.hanniel.ticketBookingSystem.config.security.TokenService;
import com.hanniel.ticketBookingSystem.domain.ticket.TicketStatus;
import com.hanniel.ticketBookingSystem.dtos.ticket.TicketRequestDTO;
import com.hanniel.ticketBookingSystem.dtos.ticket.TicketResponseDTO;
import com.hanniel.ticketBookingSystem.repositories.user.UserRepository;
import com.hanniel.ticketBookingSystem.services.ticket.TicketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TicketController.class)
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TicketService ticketService;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private SecurityFilter securityFilter;

    // --- Ticket Controller Tests ---

    @Test
    void createTicket_Success() throws Exception {
        UUID eventId = UUID.randomUUID();
        UUID ticketTypeId = UUID.randomUUID();
        OffsetDateTime date = OffsetDateTime.parse("2026-06-06T13:08:16-03:00");
        TicketResponseDTO response = new TicketResponseDTO(UUID.randomUUID(), "T-CODE-1", eventId, new BigDecimal("150.00"), ticketTypeId, TicketStatus.AVAILABLE, date, null);

        when(ticketService.createTicket(any(TicketRequestDTO.class))).thenReturn(response);

        String json = String.format("{\"ticketCode\":\"T-CODE-1\",\"eventId\":\"%s\",\"pricePaid\":150.00,\"ticketTypeId\":\"%s\",\"status\":\"AVAILABLE\",\"purchaseDate\":\"2026-06-06T13:08:16-03:00\",\"orderId\":null}", eventId, ticketTypeId);

        mockMvc.perform(post("/tickets")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ticketCode").value("T-CODE-1"));

        verify(ticketService, times(1)).createTicket(any(TicketRequestDTO.class));
    }

    @Test
    void getTicketById_Success() throws Exception {
        UUID id = UUID.randomUUID();
        TicketResponseDTO response = new TicketResponseDTO(id, "T-CODE-1", UUID.randomUUID(), new BigDecimal("150.00"), UUID.randomUUID(), TicketStatus.AVAILABLE, OffsetDateTime.now(), null);
        when(ticketService.getTicketById(id)).thenReturn(response);

        mockMvc.perform(get("/tickets/{id}", id)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketCode").value("T-CODE-1"));

        verify(ticketService, times(1)).getTicketById(id);
    }
}
