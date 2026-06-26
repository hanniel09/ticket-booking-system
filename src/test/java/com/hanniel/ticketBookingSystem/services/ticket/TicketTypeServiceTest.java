package com.hanniel.ticketBookingSystem.services.ticket;

import com.hanniel.ticketBookingSystem.domain.ticket.TicketType;
import com.hanniel.ticketBookingSystem.dtos.ticket.TicketTypeRequestDTO;
import com.hanniel.ticketBookingSystem.dtos.ticket.TicketTypeResponseDTO;
import com.hanniel.ticketBookingSystem.exceptions.global.ResourceNotFoundException;
import com.hanniel.ticketBookingSystem.mappers.ticket.TicketTypeMapper;
import com.hanniel.ticketBookingSystem.repositories.event.EventRepository;
import com.hanniel.ticketBookingSystem.repositories.ticket.TicketTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketTypeServiceTest {

    @Mock
    private TicketTypeRepository ticketTypeRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private TicketTypeMapper ticketTypeMapper;

    @InjectMocks
    private TicketTypeService ticketTypeService;

    @Test
    void createTicketType_Success() {
        UUID eventId = UUID.randomUUID();
        TicketTypeRequestDTO request = new TicketTypeRequestDTO(eventId, "VIP", new BigDecimal("150.00"), 100L);
        TicketType ticketType = new TicketType(null, eventId, request.name(), request.price(), request.quantityAvailable());
        TicketType saved = new TicketType(UUID.randomUUID(), eventId, request.name(), request.price(), request.quantityAvailable());
        TicketTypeResponseDTO expectedResponse = new TicketTypeResponseDTO(saved.getId(), eventId, "VIP", new BigDecimal("150.00"), 100L);

        when(eventRepository.existsById(eventId)).thenReturn(true);
        when(ticketTypeMapper.toEntity(any(TicketTypeRequestDTO.class))).thenReturn(ticketType);
        when(ticketTypeRepository.save(any(TicketType.class))).thenReturn(saved);
        when(ticketTypeMapper.toResponse(any(TicketType.class))).thenReturn(expectedResponse);

        TicketTypeResponseDTO response = ticketTypeService.createTicketType(request);

        assertNotNull(response);
        assertEquals(saved.getId(), response.id());
        assertEquals(request.name(), response.name());
        verify(ticketTypeRepository, times(1)).save(any(TicketType.class));
    }

    @Test
    void createTicketType_EventNotFound_ThrowsException() {
        UUID eventId = UUID.randomUUID();
        TicketTypeRequestDTO request = new TicketTypeRequestDTO(eventId, "VIP", new BigDecimal("150.00"), 100L);

        when(eventRepository.existsById(eventId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> ticketTypeService.createTicketType(request));
        verify(ticketTypeRepository, never()).save(any(TicketType.class));
    }

    @Test
    void getAllTicketTypes_Success() {
        TicketType ticketType = new TicketType(UUID.randomUUID(), UUID.randomUUID(), "Regular", new BigDecimal("50.00"), 500L);
        TicketTypeResponseDTO expectedResponse = new TicketTypeResponseDTO(ticketType.getId(), ticketType.getEventId(), "Regular", new BigDecimal("50.00"), 500L);

        when(ticketTypeRepository.findAll()).thenReturn(List.of(ticketType));
        when(ticketTypeMapper.toResponse(any(TicketType.class))).thenReturn(expectedResponse);

        List<TicketTypeResponseDTO> response = ticketTypeService.getAllTicketTypes();

        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
    }
}
