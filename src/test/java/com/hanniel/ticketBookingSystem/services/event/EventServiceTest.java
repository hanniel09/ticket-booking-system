package com.hanniel.ticketBookingSystem.services.event;

import com.hanniel.ticketBookingSystem.domain.event.Event;
import com.hanniel.ticketBookingSystem.dtos.event.EventRequestDTO;
import com.hanniel.ticketBookingSystem.dtos.event.EventResponseDTO;
import com.hanniel.ticketBookingSystem.exceptions.global.ResourceNotFoundException;
import com.hanniel.ticketBookingSystem.mappers.event.EventMapper;
import com.hanniel.ticketBookingSystem.repositories.event.EventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventService eventService;

    @Test
    void createEvent_Success() {
        EventRequestDTO request = new EventRequestDTO("Rock in Rio", OffsetDateTime.now());
        Event event = new Event(UUID.randomUUID(), request.name(), request.eventDate());
        EventResponseDTO expectedResponse = new EventResponseDTO(event.getId(), event.getName(), event.getEventDate());

        when(eventMapper.toEntity(any(EventRequestDTO.class))).thenReturn(event);
        when(eventRepository.save(any(Event.class))).thenReturn(event);
        when(eventMapper.toResponse(any(Event.class))).thenReturn(expectedResponse);

        EventResponseDTO response = eventService.createEvent(request);

        assertNotNull(response);
        assertEquals(event.getId(), response.id());
        assertEquals(request.name(), response.name());
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void getAllEvents_Success() {
        Event event = new Event(UUID.randomUUID(), "Lollapalooza", OffsetDateTime.now());
        EventResponseDTO expectedResponse = new EventResponseDTO(event.getId(), event.getName(), event.getEventDate());

        when(eventRepository.findAll()).thenReturn(List.of(event));
        when(eventMapper.toResponse(any(Event.class))).thenReturn(expectedResponse);

        List<EventResponseDTO> response = eventService.getAllEvents();

        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
        assertEquals(event.getName(), response.get(0).name());
    }

    @Test
    void getEventById_Success() {
        UUID id = UUID.randomUUID();
        Event event = new Event(id, "Tomorrowland", OffsetDateTime.now());
        EventResponseDTO expectedResponse = new EventResponseDTO(event.getId(), event.getName(), event.getEventDate());

        when(eventRepository.findById(id)).thenReturn(Optional.of(event));
        when(eventMapper.toResponse(any(Event.class))).thenReturn(expectedResponse);

        EventResponseDTO response = eventService.getEventById(id);

        assertNotNull(response);
        assertEquals(id, response.id());
    }

    @Test
    void getEventById_NotFound_ThrowsException() {
        UUID id = UUID.randomUUID();
        when(eventRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> eventService.getEventById(id));
    }

    @Test
    void updateEvent_Success() {
        UUID id = UUID.randomUUID();
        EventRequestDTO request = new EventRequestDTO("Ultra", OffsetDateTime.now());
        Event existingEvent = new Event(id, "Old Name", OffsetDateTime.now().minusDays(1));
        Event updatedEvent = new Event(id, request.name(), request.eventDate());
        EventResponseDTO expectedResponse = new EventResponseDTO(id, request.name(), request.eventDate());

        when(eventRepository.findById(id)).thenReturn(Optional.of(existingEvent));
        doNothing().when(eventMapper).updateEntityFromRequest(any(EventRequestDTO.class), any(Event.class));
        when(eventRepository.save(any(Event.class))).thenReturn(updatedEvent);
        when(eventMapper.toResponse(any(Event.class))).thenReturn(expectedResponse);

        EventResponseDTO response = eventService.updateEvent(id, request);

        assertNotNull(response);
        assertEquals(request.name(), response.name());
    }

    @Test
    void updateEvent_NotFound_ThrowsException() {
        UUID id = UUID.randomUUID();
        EventRequestDTO request = new EventRequestDTO("Ultra", OffsetDateTime.now());
        when(eventRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> eventService.updateEvent(id, request));
    }

    @Test
    void deleteEvent_Success() {
        UUID id = UUID.randomUUID();
        when(eventRepository.existsById(id)).thenReturn(true);
        doNothing().when(eventRepository).deleteById(id);

        assertDoesNotThrow(() -> eventService.deleteEvent(id));

        verify(eventRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteEvent_NotFound_ThrowsException() {
        UUID id = UUID.randomUUID();
        when(eventRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> eventService.deleteEvent(id));
    }
}
