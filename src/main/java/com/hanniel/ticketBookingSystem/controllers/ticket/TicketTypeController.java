package com.hanniel.ticketBookingSystem.controllers.ticket;

import com.hanniel.ticketBookingSystem.dtos.ticket.TicketTypeRequestDTO;
import com.hanniel.ticketBookingSystem.dtos.ticket.TicketTypeResponseDTO;
import com.hanniel.ticketBookingSystem.services.ticket.TicketTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tickets/types")
@RequiredArgsConstructor
public class TicketTypeController {

    private final TicketTypeService ticketTypeService;

    @PostMapping
    public ResponseEntity<TicketTypeResponseDTO> createTicketType(@RequestBody @Valid TicketTypeRequestDTO request) {
        TicketTypeResponseDTO created = ticketTypeService.createTicketType(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<TicketTypeResponseDTO>> getAllTicketTypes() {
        List<TicketTypeResponseDTO> types = ticketTypeService.getAllTicketTypes();
        return ResponseEntity.ok(types);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketTypeResponseDTO> getTicketTypeById(@PathVariable UUID id) {
        TicketTypeResponseDTO type = ticketTypeService.getTicketTypeById(id);
        return ResponseEntity.ok(type);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketTypeResponseDTO> updateTicketType(@PathVariable UUID id, @RequestBody @Valid TicketTypeRequestDTO request) {
        TicketTypeResponseDTO updated = ticketTypeService.updateTicketType(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicketType(@PathVariable UUID id) {
        ticketTypeService.deleteTicketType(id);
        return ResponseEntity.noContent().build();
    }
}
