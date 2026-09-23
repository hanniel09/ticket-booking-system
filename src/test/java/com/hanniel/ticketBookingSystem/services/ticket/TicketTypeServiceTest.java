package com.hanniel.ticketBookingSystem.services.ticket;

import com.hanniel.ticketBookingSystem.domain.ticket.TicketType;
import com.hanniel.ticketBookingSystem.dtos.ticket.TicketTypeRequestDTO;
import com.hanniel.ticketBookingSystem.dtos.ticket.TicketTypeResponseDTO;
import com.hanniel.ticketBookingSystem.exceptions.global.ResourceNotFoundException;
import com.hanniel.ticketBookingSystem.mappers.ticket.TicketTypeMapper;
import com.hanniel.ticketBookingSystem.repositories.event.EventRepository;
import com.hanniel.ticketBookingSystem.repositories.ticket.TicketTypeRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

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
        assertEquals(request.price(), response.price());
        assertEquals(request.quantityAvailable(), response.quantityAvailable());
        verify(ticketTypeRepository, times(1)).save(any(TicketType.class));
    }

    @Test
    void createTicketType_EventNotFound_ThrowsException() {
        UUID eventId = UUID.randomUUID();
        TicketTypeRequestDTO request = new TicketTypeRequestDTO(eventId, "VIP", new BigDecimal("150.00"), 100L);

        when(eventRepository.existsById(eventId)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> ticketTypeService.createTicketType(request)
        );

        assertEquals("Evento não encontrado", exception.getMessage());
        verify(ticketTypeRepository, never()).save(any(TicketType.class));
    }

    @Test
    void findById_Success() {
        UUID id = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        TicketType ticketType = new TicketType(id, eventId, "VIP", new BigDecimal("150.00"), 100L);
        TicketTypeResponseDTO expectedResponse = new TicketTypeResponseDTO(id, eventId, "VIP", new BigDecimal("150.00"), 100L);

        when(ticketTypeRepository.findById(id)).thenReturn(Optional.of(ticketType));
        when(ticketTypeMapper.toResponse(ticketType)).thenReturn(expectedResponse);

        TicketTypeResponseDTO response = ticketTypeService.findById(id);

        assertNotNull(response);
        assertEquals(id, response.id());
        assertEquals("VIP", response.name());
        verify(ticketTypeRepository, times(1)).findById(id);
    }

    @Test
    void findById_NotFound_ThrowsException() {
        UUID id = UUID.randomUUID();
        when(ticketTypeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ticketTypeService.findById(id));
        verify(ticketTypeRepository, times(1)).findById(id);
    }

    @Test
    void getTicketTypeById_Success() {
        UUID id = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        TicketType ticketType = new TicketType(id, eventId, "Pista", new BigDecimal("80.00"), 500L);
        TicketTypeResponseDTO expectedResponse = new TicketTypeResponseDTO(id, eventId, "Pista", new BigDecimal("80.00"), 500L);

        when(ticketTypeRepository.findById(id)).thenReturn(Optional.of(ticketType));
        when(ticketTypeMapper.toResponse(ticketType)).thenReturn(expectedResponse);

        TicketTypeResponseDTO response = ticketTypeService.getTicketTypeById(id);

        assertNotNull(response);
        assertEquals(id, response.id());
        assertEquals("Pista", response.name());
        verify(ticketTypeRepository, times(1)).findById(id);
    }

    @Test
    void getTicketTypeById_NotFound_ThrowsException() {
        UUID id = UUID.randomUUID();
        when(ticketTypeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ticketTypeService.getTicketTypeById(id));
        verify(ticketTypeRepository, times(1)).findById(id);
    }

    @Test
    void findByEventId_Success() {
        UUID eventId = UUID.randomUUID();
        TicketType ticketType1 = new TicketType(UUID.randomUUID(), eventId, "Pista", new BigDecimal("80.00"), 300L);
        TicketType ticketType2 = new TicketType(UUID.randomUUID(), eventId, "VIP", new BigDecimal("160.00"), 100L);
        TicketTypeResponseDTO dto1 = new TicketTypeResponseDTO(ticketType1.getId(), eventId, "Pista", new BigDecimal("80.00"), 300L);
        TicketTypeResponseDTO dto2 = new TicketTypeResponseDTO(ticketType2.getId(), eventId, "VIP", new BigDecimal("160.00"), 100L);

        when(ticketTypeRepository.findByEventId(eventId)).thenReturn(List.of(ticketType1, ticketType2));
        when(ticketTypeMapper.toResponse(ticketType1)).thenReturn(dto1);
        when(ticketTypeMapper.toResponse(ticketType2)).thenReturn(dto2);

        List<TicketTypeResponseDTO> response = ticketTypeService.findByEventId(eventId);

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("Pista", response.get(0).name());
        assertEquals("VIP", response.get(1).name());
        verify(ticketTypeRepository, times(1)).findByEventId(eventId);
    }

    @Test
    void findByEventId_EmptyList() {
        UUID eventId = UUID.randomUUID();
        when(ticketTypeRepository.findByEventId(eventId)).thenReturn(List.of());

        List<TicketTypeResponseDTO> response = ticketTypeService.findByEventId(eventId);

        assertNotNull(response);
        assertTrue(response.isEmpty());
        verify(ticketTypeRepository, times(1)).findByEventId(eventId);
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
        verify(ticketTypeRepository, times(1)).findAll();
    }

    @Test
    void updateTicketType_Success() {
        UUID id = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        TicketTypeRequestDTO request = new TicketTypeRequestDTO(eventId, "VIP Atualizado", new BigDecimal("200.00"), 150L);
        TicketType existingTicketType = new TicketType(id, eventId, "VIP", new BigDecimal("150.00"), 100L);
        TicketType updatedTicketType = new TicketType(id, eventId, "VIP Atualizado", new BigDecimal("200.00"), 150L);
        TicketTypeResponseDTO expectedResponse = new TicketTypeResponseDTO(id, eventId, "VIP Atualizado", new BigDecimal("200.00"), 150L);

        when(ticketTypeRepository.findById(id)).thenReturn(Optional.of(existingTicketType));
        when(eventRepository.existsById(eventId)).thenReturn(true);
        doNothing().when(ticketTypeMapper).updateEntityFromRequest(any(TicketTypeRequestDTO.class), any(TicketType.class));
        when(ticketTypeRepository.save(any(TicketType.class))).thenReturn(updatedTicketType);
        when(ticketTypeMapper.toResponse(any(TicketType.class))).thenReturn(expectedResponse);

        TicketTypeResponseDTO response = ticketTypeService.updateTicketType(id, request);

        assertNotNull(response);
        assertEquals(request.name(), response.name());
        assertEquals(request.price(), response.price());
        verify(ticketTypeRepository, times(1)).save(any(TicketType.class));
    }

    @Test
    void updateTicketType_NotFound_ThrowsException() {
        UUID id = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        TicketTypeRequestDTO request = new TicketTypeRequestDTO(eventId, "VIP", new BigDecimal("150.00"), 100L);

        when(ticketTypeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ticketTypeService.updateTicketType(id, request));
        verify(ticketTypeRepository, never()).save(any(TicketType.class));
    }

    @Test
    void updateTicketType_EventNotFound_ThrowsException() {
        UUID id = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        TicketTypeRequestDTO request = new TicketTypeRequestDTO(eventId, "VIP", new BigDecimal("150.00"), 100L);
        TicketType existingTicketType = new TicketType(id, eventId, "VIP", new BigDecimal("150.00"), 100L);

        when(ticketTypeRepository.findById(id)).thenReturn(Optional.of(existingTicketType));
        when(eventRepository.existsById(eventId)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> ticketTypeService.updateTicketType(id, request)
        );

        assertEquals("Evento não encontrado", exception.getMessage());
        verify(ticketTypeRepository, never()).save(any(TicketType.class));
    }

    @Test
    void deleteTicketType_Success() {
        UUID id = UUID.randomUUID();
        when(ticketTypeRepository.existsById(id)).thenReturn(true);
        doNothing().when(ticketTypeRepository).deleteById(id);

        assertDoesNotThrow(() -> ticketTypeService.deleteTicketType(id));
        verify(ticketTypeRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteTicketType_NotFound_ThrowsException() {
        UUID id = UUID.randomUUID();
        when(ticketTypeRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> ticketTypeService.deleteTicketType(id));
        verify(ticketTypeRepository, never()).deleteById(any(UUID.class));
    }

    @Test
    void ticketTypeRequestDTO_Validation_Success() {
        TicketTypeRequestDTO request = new TicketTypeRequestDTO(
                UUID.randomUUID(),
                "Pista Premium",
                new BigDecimal("120.00"),
                50L
        );
        Set<ConstraintViolation<TicketTypeRequestDTO>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void ticketTypeRequestDTO_Validation_NullEventId() {
        TicketTypeRequestDTO request = new TicketTypeRequestDTO(
                null,
                "Pista Premium",
                new BigDecimal("120.00"),
                50L
        );
        Set<ConstraintViolation<TicketTypeRequestDTO>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("O ID do evento é obrigatório")));
    }

    @Test
    void ticketTypeRequestDTO_Validation_BlankName() {
        TicketTypeRequestDTO request = new TicketTypeRequestDTO(
                UUID.randomUUID(),
                "   ",
                new BigDecimal("120.00"),
                50L
        );
        Set<ConstraintViolation<TicketTypeRequestDTO>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("O nome da categoria do ingresso é obrigatório")));
    }

    @Test
    void ticketTypeRequestDTO_Validation_NegativePrice() {
        TicketTypeRequestDTO request = new TicketTypeRequestDTO(
                UUID.randomUUID(),
                "Pista",
                new BigDecimal("-10.00"),
                50L
        );
        Set<ConstraintViolation<TicketTypeRequestDTO>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("O preço não pode ser negativo")));
    }

    @Test
    void ticketTypeRequestDTO_Validation_ZeroQuantity() {
        TicketTypeRequestDTO request = new TicketTypeRequestDTO(
                UUID.randomUUID(),
                "Pista",
                new BigDecimal("10.00"),
                0L
        );
        Set<ConstraintViolation<TicketTypeRequestDTO>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("A quantidade inicial deve ser de no mínimo 1 ingresso")));
    }
}
