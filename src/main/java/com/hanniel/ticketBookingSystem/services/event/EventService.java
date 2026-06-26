package com.hanniel.ticketBookingSystem.services.event;

import com.hanniel.ticketBookingSystem.domain.event.Event;
import com.hanniel.ticketBookingSystem.dtos.event.EventRequestDTO;
import com.hanniel.ticketBookingSystem.dtos.event.EventResponseDTO;
import com.hanniel.ticketBookingSystem.exceptions.global.ResourceNotFoundException;
import com.hanniel.ticketBookingSystem.mappers.event.EventMapper;
import com.hanniel.ticketBookingSystem.repositories.event.EventRepository;
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
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Transactional
    public EventResponseDTO createEvent(EventRequestDTO request) {
        log.info("Creating event with name: {}", request.name());
        Event event = eventMapper.toEntity(request);
        Event savedEvent = eventRepository.save(event);
        log.info("Event created successfully with ID: {}", savedEvent.getId());
        return eventMapper.toResponse(savedEvent);
    }

    @Transactional(readOnly = true)
    public List<EventResponseDTO> getAllEvents() {
        log.info("Retrieving all events");
        return eventRepository.findAll().stream()
                .map(eventMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EventResponseDTO getEventById(UUID id) {
        log.info("Retrieving event with ID: {}", id);
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with ID: " + id));
        return eventMapper.toResponse(event);
    }

    @Transactional
    public EventResponseDTO updateEvent(UUID id, EventRequestDTO request) {
        log.info("Updating event with ID: {}", id);
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with ID: " + id));

        eventMapper.updateEntityFromRequest(request, event);

        Event updatedEvent = eventRepository.save(event);
        log.info("Event updated successfully with ID: {}", updatedEvent.getId());
        return eventMapper.toResponse(updatedEvent);
    }

    @Transactional
    public void deleteEvent(UUID id) {
        log.info("Deleting event with ID: {}", id);
        if (!eventRepository.existsById(id)) {
            throw new ResourceNotFoundException("Event not found with ID: " + id);
        }
        eventRepository.deleteById(id);
        log.info("Event deleted successfully with ID: {}", id);
    }
}
