package com.hanniel.ticketBookingSystem.controllers.billingAddress;

import com.hanniel.ticketBookingSystem.domain.user.User;
import com.hanniel.ticketBookingSystem.dtos.billingAddress.BillingAddressRequestDTO;
import com.hanniel.ticketBookingSystem.dtos.billingAddress.BillingAddressResponseDTO;
import com.hanniel.ticketBookingSystem.services.billingAddress.BillingAddressService;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/billing-addresses")
@RequiredArgsConstructor
@Tag(name = "Endereço de Cobrança", description = "Gerenciamento de dados fiscais do usuário")
@SecurityRequirement(name = "bearerAuth")
public class BillingAddressController {

    private final BillingAddressService billingAddressService;

    @Operation(summary = "Criar endereço de cobrança", description = "Cadastra um novo endereço de cobrança vinculado ao usuário autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Endereço criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    @PostMapping
    public ResponseEntity<BillingAddressResponseDTO> createBillingAddress(
            @RequestBody @Valid BillingAddressRequestDTO request,
            @AuthenticationPrincipal User currentUser) {
        log.info("Received request to create billing address for user: {}", currentUser != null ? currentUser.getUsername() : "unknown");
        BillingAddressResponseDTO created = billingAddressService.createBillingAddress(request, currentUser);
        log.info("Billing address created with ID: {}", created.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Listar endereços de cobrança", description = "Recupera todos os endereços de cobrança do usuário autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    @GetMapping
    public ResponseEntity<List<BillingAddressResponseDTO>> getUserBillingAddresses(
            @AuthenticationPrincipal User currentUser) {
        log.info("Received request to list billing addresses for user: {}", currentUser != null ? currentUser.getUsername() : "unknown");
        List<BillingAddressResponseDTO> addresses = billingAddressService.getUserBillingAddresses(currentUser);
        return ResponseEntity.ok(addresses);
    }

    @Operation(summary = "Buscar endereço por ID", description = "Recupera os detalhes de um endereço de cobrança específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereço encontrado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BillingAddressResponseDTO> getBillingAddressById(
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser) {
        log.info("Received request to find billing address ID: {}", id);
        BillingAddressResponseDTO address = billingAddressService.getBillingAddressById(id, currentUser);
        return ResponseEntity.ok(address);
    }

    @Operation(summary = "Atualizar endereço de cobrança", description = "Atualiza os dados de um endereço de cobrança existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereço atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BillingAddressResponseDTO> updateBillingAddress(
            @PathVariable UUID id,
            @RequestBody @Valid BillingAddressRequestDTO request,
            @AuthenticationPrincipal User currentUser) {
        log.info("Received request to update billing address ID: {}", id);
        BillingAddressResponseDTO updated = billingAddressService.updateBillingAddress(id, request, currentUser);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Deletar endereço de cobrança", description = "Remove um endereço de cobrança do usuário autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Endereço deletado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBillingAddress(
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser) {
        log.info("Received request to delete billing address ID: {}", id);
        billingAddressService.deleteBillingAddress(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}
