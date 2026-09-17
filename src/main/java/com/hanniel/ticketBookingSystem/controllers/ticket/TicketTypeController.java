package com.hanniel.ticketBookingSystem.controllers.ticket;

import com.hanniel.ticketBookingSystem.dtos.ticket.TicketTypeRequestDTO;
import com.hanniel.ticketBookingSystem.dtos.ticket.TicketTypeResponseDTO;
import com.hanniel.ticketBookingSystem.services.ticket.TicketTypeService;
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
@RequestMapping("/tickets/types")
@RequiredArgsConstructor
@Tag(name = "Tipos de Ingresso", description = "Gestão de lotes e estoque de ingressos")
@SecurityRequirement(name = "bearerAuth")
public class TicketTypeController {

    private final TicketTypeService ticketTypeService;

    @Operation(summary = "Criar tipo de ingresso", description = "Cadastra uma nova categoria de ingresso associada a um evento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tipo de ingresso criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    @PostMapping
    public ResponseEntity<TicketTypeResponseDTO> createTicketType(@RequestBody @Valid TicketTypeRequestDTO request) {
        log.info("Received request to create ticket type: {} for event ID: {}", request.name(), request.eventId());
        TicketTypeResponseDTO created = ticketTypeService.createTicketType(request);
        log.info("Ticket type created successfully with ID: {}", created.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Listar tipos de ingresso", description = "Retorna todos os tipos de ingresso disponíveis")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    @GetMapping
    public ResponseEntity<List<TicketTypeResponseDTO>> getAllTicketTypes() {
        log.info("Received request to list all ticket types");
        List<TicketTypeResponseDTO> types = ticketTypeService.getAllTicketTypes();
        return ResponseEntity.ok(types);
    }

    @Operation(summary = "Buscar tipo de ingresso por ID", description = "Retorna os detalhes de um tipo de ingresso específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de ingresso retornado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "404", description = "Tipo de ingresso não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TicketTypeResponseDTO> getTicketTypeById(@PathVariable UUID id) {
        log.info("Received request to get ticket type by ID: {}", id);
        TicketTypeResponseDTO type = ticketTypeService.getTicketTypeById(id);
        return ResponseEntity.ok(type);
    }

    @Operation(summary = "Atualizar tipo de ingresso", description = "Atualiza os dados de um tipo de ingresso existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de ingresso atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "404", description = "Tipo de ingresso não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TicketTypeResponseDTO> updateTicketType(@PathVariable UUID id, @RequestBody @Valid TicketTypeRequestDTO request) {
        log.info("Received request to update ticket type ID: {}", id);
        TicketTypeResponseDTO updated = ticketTypeService.updateTicketType(id, request);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Deletar tipo de ingresso", description = "Remove um tipo de ingresso pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tipo de ingresso deletado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "404", description = "Tipo de ingresso não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicketType(@PathVariable UUID id) {
        log.info("Received request to delete ticket type ID: {}", id);
        ticketTypeService.deleteTicketType(id);
        return ResponseEntity.noContent().build();
    }
}
