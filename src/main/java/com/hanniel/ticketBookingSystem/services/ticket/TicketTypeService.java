package com.hanniel.ticketBookingSystem.services.ticket;

import com.hanniel.ticketBookingSystem.domain.ticket.TicketType;
import com.hanniel.ticketBookingSystem.dtos.ticket.TicketTypeRequestDTO;
import com.hanniel.ticketBookingSystem.dtos.ticket.TicketTypeResponseDTO;
import com.hanniel.ticketBookingSystem.exceptions.global.ResourceNotFoundException;
import com.hanniel.ticketBookingSystem.mappers.ticket.TicketTypeMapper;
import com.hanniel.ticketBookingSystem.repositories.event.EventRepository;
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
public class TicketTypeService {

    private final TicketTypeRepository ticketTypeRepository;
    private final EventRepository eventRepository;
    private final TicketTypeMapper ticketTypeMapper;

    @Transactional
    public TicketTypeResponseDTO createTicketType(TicketTypeRequestDTO request) {
        log.info("Creating ticket type with name: {} for event ID: {}", request.name(), request.eventId());
        if (!eventRepository.existsById(request.eventId())) {
            throw new ResourceNotFoundException("Event not found with ID: " + request.eventId());
        }

        TicketType ticketType = ticketTypeMapper.toEntity(request);
        TicketType saved = ticketTypeRepository.save(ticketType);
        log.info("Ticket type created successfully with ID: {}", saved.getId());
        return ticketTypeMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<TicketTypeResponseDTO> getAllTicketTypes() {
        log.info("Retrieving all ticket types");
        return ticketTypeRepository.findAll().stream()
                .map(ticketTypeMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TicketTypeResponseDTO getTicketTypeById(UUID id) {
        log.info("Retrieving ticket type with ID: {}", id);
        TicketType ticketType = ticketTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket type not found with ID: " + id));
        return ticketTypeMapper.toResponse(ticketType);
    }

    @Transactional
    public TicketTypeResponseDTO updateTicketType(UUID id, TicketTypeRequestDTO request) {
        log.info("Updating ticket type with ID: {}", id);
        TicketType ticketType = ticketTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket type not found with ID: " + id));

        if (!eventRepository.existsById(request.eventId())) {
            throw new ResourceNotFoundException("Event not found with ID: " + request.eventId());
        }

        ticketTypeMapper.updateEntityFromRequest(request, ticketType);

        TicketType updated = ticketTypeRepository.save(ticketType);
        log.info("Ticket type updated successfully with ID: {}", updated.getId());
        return ticketTypeMapper.toResponse(updated);
    }

    @Transactional
    public void deleteTicketType(UUID id) {
        log.info("Deleting ticket type with ID: {}", id);
        if (!ticketTypeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ticket type not found with ID: " + id);
        }
        ticketTypeRepository.deleteById(id);
        log.info("Ticket type deleted successfully with ID: {}", id);
    }
}
