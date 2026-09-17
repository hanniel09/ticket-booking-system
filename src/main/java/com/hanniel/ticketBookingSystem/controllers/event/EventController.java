package com.hanniel.ticketBookingSystem.controllers.event;

import com.hanniel.ticketBookingSystem.dtos.event.EventRequestDTO;
import com.hanniel.ticketBookingSystem.dtos.event.EventResponseDTO;
import com.hanniel.ticketBookingSystem.services.event.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Tag(name = "Eventos", description = "Consulta e catálogo de eventos disponíveis")
@SecurityRequirement(name = "bearerAuth")
public class EventController {

    private final EventService eventService;

    @Operation(summary = "Criar novo evento", description = "Cadastra um novo evento no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Evento criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    @PostMapping
    public ResponseEntity<EventResponseDTO> createEvent(@RequestBody @Valid EventRequestDTO request) {
        log.info("Received request to create event: {}", request.name());
        EventResponseDTO created = eventService.createEvent(request);
        log.info("Event created successfully with ID: {}", created.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Listar eventos", description = "Retorna todos os eventos cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de eventos retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getAllEvents() {
        log.info("Received request to list all events");
        List<EventResponseDTO> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @Operation(summary = "Buscar evento por ID", description = "Retorna os detalhes de um evento específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento retornado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "404", description = "Evento não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable UUID id) {
        log.info("Received request to get event by ID: {}", id);
        EventResponseDTO event = eventService.getEventById(id);
        return ResponseEntity.ok(event);
    }

    @Operation(summary = "Atualizar evento", description = "Atualiza as informações de um evento existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "404", description = "Evento não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDTO> updateEvent(@PathVariable UUID id, @RequestBody @Valid EventRequestDTO request) {
        log.info("Received request to update event ID: {}", id);
        EventResponseDTO updated = eventService.updateEvent(id, request);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Deletar evento", description = "Remove um evento pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Evento deletado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "404", description = "Evento não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID id) {
        log.info("Received request to delete event ID: {}", id);
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
