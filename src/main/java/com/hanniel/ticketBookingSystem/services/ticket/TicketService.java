package com.hanniel.ticketBookingSystem.services.ticket;

import com.hanniel.ticketBookingSystem.domain.order.Order;
import com.hanniel.ticketBookingSystem.domain.ticket.Ticket;
import com.hanniel.ticketBookingSystem.domain.ticket.TicketType;
import com.hanniel.ticketBookingSystem.dtos.ticket.TicketRequestDTO;
import com.hanniel.ticketBookingSystem.dtos.ticket.TicketResponseDTO;
import com.hanniel.ticketBookingSystem.exceptions.global.ResourceNotFoundException;
import com.hanniel.ticketBookingSystem.mappers.ticket.TicketMapper;
import com.hanniel.ticketBookingSystem.repositories.event.EventRepository;
import com.hanniel.ticketBookingSystem.repositories.order.OrderRepository;
import com.hanniel.ticketBookingSystem.repositories.ticket.TicketRepository;
import com.hanniel.ticketBookingSystem.repositories.ticket.TicketTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final EventRepository eventRepository;
    private final OrderRepository orderRepository;
    private final TicketMapper ticketMapper;

    // --- Ticket CRUD ---

    @Transactional
    public TicketResponseDTO createTicket(TicketRequestDTO request) {
        log.info("Creating ticket with code: {}", request.ticketCode());
        if (!eventRepository.existsById(request.eventId())) {
            throw new ResourceNotFoundException("Event not found with ID: " + request.eventId());
        }

        TicketType ticketType = ticketTypeRepository.findById(request.ticketTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Ticket type not found with ID: " + request.ticketTypeId()));

        Ticket ticket = ticketMapper.toEntity(request);
        ticket.setTicketType(ticketType);

        if (request.orderId() != null) {
            Order order = orderRepository.findById(request.orderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + request.orderId()));
            ticket.setOrder(order);
        }

        Ticket saved = ticketRepository.save(ticket);
        log.info("Ticket created successfully with ID: {}", saved.getId());
        return ticketMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<TicketResponseDTO> getAllTickets() {
        log.info("Retrieving all tickets");
        return ticketRepository.findAll().stream()
                .map(ticketMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TicketResponseDTO getTicketById(UUID id) {
        log.info("Retrieving ticket with ID: {}", id);
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with ID: " + id));
        return ticketMapper.toResponse(ticket);
    }

    @Transactional
    public TicketResponseDTO updateTicket(UUID id, TicketRequestDTO request) {
        log.info("Updating ticket with ID: {}", id);
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with ID: " + id));

        if (!eventRepository.existsById(request.eventId())) {
            throw new ResourceNotFoundException("Event not found with ID: " + request.eventId());
        }

        TicketType ticketType = ticketTypeRepository.findById(request.ticketTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Ticket type not found with ID: " + request.ticketTypeId()));

        ticketMapper.updateEntityFromRequest(request, ticket);
        ticket.setTicketType(ticketType);

        if (request.orderId() != null) {
            Order order = orderRepository.findById(request.orderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + request.orderId()));
            ticket.setOrder(order);
        } else {
            ticket.setOrder(null);
        }

        Ticket updated = ticketRepository.save(ticket);
        log.info("Ticket updated successfully with ID: {}", updated.getId());
        return ticketMapper.toResponse(updated);
    }

    @Transactional
    public void deleteTicket(UUID id) {
        log.info("Deleting ticket with ID: {}", id);
        if (!ticketRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ticket not found with ID: " + id);
        }
        ticketRepository.deleteById(id);
        log.info("Ticket deleted successfully with ID: {}", id);
    }
}
