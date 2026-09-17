package com.hanniel.ticketBookingSystem.controllers.ticket;

import com.hanniel.ticketBookingSystem.dtos.ticket.TicketRequestDTO;
import com.hanniel.ticketBookingSystem.dtos.ticket.TicketResponseDTO;
import com.hanniel.ticketBookingSystem.services.ticket.TicketService;
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
@RequestMapping("/tickets")
@RequiredArgsConstructor
@Tag(name = "Ingressos", description = "Emissão e consulta de ingressos do usuário")
@SecurityRequirement(name = "bearerAuth")
public class TicketController {

    private final TicketService ticketService;

    @Operation(summary = "Criar novo ingresso", description = "Emite um novo ingresso no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ingresso criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    @PostMapping
    public ResponseEntity<TicketResponseDTO> createTicket(@RequestBody @Valid TicketRequestDTO request) {
        log.info("Received request to create ticket with code: {}", request.ticketCode());
        TicketResponseDTO created = ticketService.createTicket(request);
        log.info("Ticket created successfully with ID: {}", created.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Listar ingressos", description = "Retorna todos os ingressos cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    @GetMapping
    public ResponseEntity<List<TicketResponseDTO>> getAllTickets() {
        log.info("Received request to list all tickets");
        List<TicketResponseDTO> tickets = ticketService.getAllTickets();
        return ResponseEntity.ok(tickets);
    }

    @Operation(summary = "Buscar ingresso por ID", description = "Retorna as informações de um ingresso específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ingresso retornado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "404", description = "Ingresso não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> getTicketById(@PathVariable UUID id) {
        log.info("Received request to get ticket by ID: {}", id);
        TicketResponseDTO ticket = ticketService.getTicketById(id);
        return ResponseEntity.ok(ticket);
    }

    @Operation(summary = "Atualizar ingresso", description = "Atualiza os dados de um ingresso existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ingresso atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "404", description = "Ingresso não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> updateTicket(@PathVariable UUID id, @RequestBody @Valid TicketRequestDTO request) {
        log.info("Received request to update ticket ID: {}", id);
        TicketResponseDTO updated = ticketService.updateTicket(id, request);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Deletar ingresso", description = "Remove um ingresso do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ingresso deletado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "404", description = "Ingresso não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable UUID id) {
        log.info("Received request to delete ticket ID: {}", id);
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }
}
