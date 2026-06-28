package com.hanniel.ticketBookingSystem.controllers.event;

import com.hanniel.ticketBookingSystem.config.security.SecurityFilter;
import com.hanniel.ticketBookingSystem.config.security.TokenService;
import com.hanniel.ticketBookingSystem.dtos.event.EventRequestDTO;
import com.hanniel.ticketBookingSystem.dtos.event.EventResponseDTO;
import com.hanniel.ticketBookingSystem.repositories.user.UserRepository;
import com.hanniel.ticketBookingSystem.services.event.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EventService eventService;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private SecurityFilter securityFilter;

    @Test
    void createEvent_Success() throws Exception {
        UUID eventId = UUID.randomUUID();
        EventResponseDTO response = new EventResponseDTO(eventId, "Concert", "06/06/2026");

        when(eventService.createEvent(any(EventRequestDTO.class))).thenReturn(response);

        String json = "{\"name\":\"Concert\",\"eventDate\":\"06/06/2026\"}";

        mockMvc.perform(post("/events")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Concert"));

        verify(eventService, times(1)).createEvent(any(EventRequestDTO.class));
    }

    @Test
    void getAllEvents_Success() throws Exception {
        EventResponseDTO response = new EventResponseDTO(UUID.randomUUID(), "Concert", "06/06/2026");
        when(eventService.getAllEvents()).thenReturn(List.of(response));

        mockMvc.perform(get("/events")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Concert"));

        verify(eventService, times(1)).getAllEvents();
    }

    @Test
    void getEventById_Success() throws Exception {
        UUID id = UUID.randomUUID();
        EventResponseDTO response = new EventResponseDTO(id, "Concert", "06/06/2026");
        when(eventService.getEventById(id)).thenReturn(response);

        mockMvc.perform(get("/events/{id}", id)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));

        verify(eventService, times(1)).getEventById(id);
    }

    @Test
    void updateEvent_Success() throws Exception {
        UUID id = UUID.randomUUID();
        EventResponseDTO response = new EventResponseDTO(id, "New Concert", "06/06/2026");

        when(eventService.updateEvent(eq(id), any(EventRequestDTO.class))).thenReturn(response);

        String json = "{\"name\":\"New Concert\",\"eventDate\":\"06/06/2026\"}";

        mockMvc.perform(put("/events/{id}", id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Concert"));

        verify(eventService, times(1)).updateEvent(eq(id), any(EventRequestDTO.class));
    }

    @Test
    void deleteEvent_Success() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(eventService).deleteEvent(id);

        mockMvc.perform(delete("/events/{id}", id)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(eventService, times(1)).deleteEvent(id);
    }
}
