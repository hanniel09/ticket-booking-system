package com.hanniel.ticketBookingSystem.services.ticket;

import com.hanniel.ticketBookingSystem.domain.order.Order;
import com.hanniel.ticketBookingSystem.domain.ticket.Ticket;
import com.hanniel.ticketBookingSystem.domain.ticket.TicketStatus;
import com.hanniel.ticketBookingSystem.domain.ticket.TicketType;
import com.hanniel.ticketBookingSystem.dtos.ticket.TicketRequestDTO;
import com.hanniel.ticketBookingSystem.dtos.ticket.TicketResponseDTO;
import com.hanniel.ticketBookingSystem.exceptions.global.ResourceNotFoundException;
import com.hanniel.ticketBookingSystem.mappers.ticket.TicketMapper;
import com.hanniel.ticketBookingSystem.repositories.event.EventRepository;
import com.hanniel.ticketBookingSystem.repositories.order.OrderRepository;
import com.hanniel.ticketBookingSystem.repositories.ticket.TicketRepository;
import com.hanniel.ticketBookingSystem.repositories.ticket.TicketTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private TicketTypeRepository ticketTypeRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private TicketMapper ticketMapper;

    @InjectMocks
    private TicketService ticketService;

    // --- Ticket Tests ---

    @Test
    void createTicket_Success() {
        UUID eventId = UUID.randomUUID();
        UUID ticketTypeId = UUID.randomUUID();
        TicketRequestDTO request = new TicketRequestDTO("T-12345", eventId, new BigDecimal("100.00"), ticketTypeId, TicketStatus.AVAILABLE, OffsetDateTime.now(), null);

        TicketType ticketType = new TicketType(ticketTypeId, eventId, "VIP", new BigDecimal("100.00"), 100L);
        Ticket ticket = new Ticket(null, request.ticketCode(), eventId, request.pricePaid(), ticketType, request.status(), request.purchaseDate(), null);
        Ticket saved = new Ticket(UUID.randomUUID(), request.ticketCode(), eventId, request.pricePaid(), ticketType, request.status(), request.purchaseDate(), null);
        TicketResponseDTO expectedResponse = new TicketResponseDTO(saved.getId(), request.ticketCode(), eventId, request.pricePaid(), ticketTypeId, request.status(), request.purchaseDate(), null);

        when(eventRepository.existsById(eventId)).thenReturn(true);
        when(ticketTypeRepository.findById(ticketTypeId)).thenReturn(Optional.of(ticketType));
        when(ticketMapper.toEntity(any(TicketRequestDTO.class))).thenReturn(ticket);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(saved);
        when(ticketMapper.toResponse(any(Ticket.class))).thenReturn(expectedResponse);

        TicketResponseDTO response = ticketService.createTicket(request);

        assertNotNull(response);
        assertEquals(saved.getId(), response.id());
        assertEquals(request.ticketCode(), response.ticketCode());
    }

    @Test
    void createTicket_TicketTypeNotFound_ThrowsException() {
        UUID eventId = UUID.randomUUID();
        UUID ticketTypeId = UUID.randomUUID();
        TicketRequestDTO request = new TicketRequestDTO("T-12345", eventId, new BigDecimal("100.00"), ticketTypeId, TicketStatus.AVAILABLE, OffsetDateTime.now(), null);

        when(eventRepository.existsById(eventId)).thenReturn(true);
        when(ticketTypeRepository.findById(ticketTypeId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ticketService.createTicket(request));
        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    @Test
    void createTicket_WithOrder_Success() {
        UUID eventId = UUID.randomUUID();
        UUID ticketTypeId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        TicketRequestDTO request = new TicketRequestDTO("T-12345", eventId, new BigDecimal("100.00"), ticketTypeId, TicketStatus.RESERVED, OffsetDateTime.now(), orderId);

        TicketType ticketType = new TicketType(ticketTypeId, eventId, "VIP", new BigDecimal("100.00"), 100L);
        Order order = new Order();
        order.setId(orderId);
        Ticket ticket = new Ticket(null, request.ticketCode(), eventId, request.pricePaid(), ticketType, request.status(), request.purchaseDate(), null);
        Ticket saved = new Ticket(UUID.randomUUID(), request.ticketCode(), eventId, request.pricePaid(), ticketType, request.status(), request.purchaseDate(), order);
        TicketResponseDTO expectedResponse = new TicketResponseDTO(saved.getId(), request.ticketCode(), eventId, request.pricePaid(), ticketTypeId, request.status(), request.purchaseDate(), orderId);

        when(eventRepository.existsById(eventId)).thenReturn(true);
        when(ticketTypeRepository.findById(ticketTypeId)).thenReturn(Optional.of(ticketType));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(ticketMapper.toEntity(any(TicketRequestDTO.class))).thenReturn(ticket);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(saved);
        when(ticketMapper.toResponse(any(Ticket.class))).thenReturn(expectedResponse);

        TicketResponseDTO response = ticketService.createTicket(request);

        assertNotNull(response);
        assertEquals(orderId, response.orderId());
    }
}
