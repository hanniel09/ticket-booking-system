package com.hanniel.ticketBookingSystem.controllers.billingAddress;

import com.hanniel.ticketBookingSystem.domain.user.User;
import com.hanniel.ticketBookingSystem.dtos.billingAddress.BillingAddressRequestDTO;
import com.hanniel.ticketBookingSystem.dtos.billingAddress.BillingAddressResponseDTO;
import com.hanniel.ticketBookingSystem.services.billingAddress.BillingAddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/billing-addresses")
@RequiredArgsConstructor
public class BillingAddressController {

    private final BillingAddressService billingAddressService;

    @PostMapping
    public ResponseEntity<BillingAddressResponseDTO> createBillingAddress(
            @RequestBody @Valid BillingAddressRequestDTO request,
            @AuthenticationPrincipal User currentUser) {
        BillingAddressResponseDTO created = billingAddressService.createBillingAddress(request, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<BillingAddressResponseDTO>> getUserBillingAddresses(
            @AuthenticationPrincipal User currentUser) {
        List<BillingAddressResponseDTO> addresses = billingAddressService.getUserBillingAddresses(currentUser);
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillingAddressResponseDTO> getBillingAddressById(
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser) {
        BillingAddressResponseDTO address = billingAddressService.getBillingAddressById(id, currentUser);
        return ResponseEntity.ok(address);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BillingAddressResponseDTO> updateBillingAddress(
            @PathVariable UUID id,
            @RequestBody @Valid BillingAddressRequestDTO request,
            @AuthenticationPrincipal User currentUser) {
        BillingAddressResponseDTO updated = billingAddressService.updateBillingAddress(id, request, currentUser);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBillingAddress(
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser) {
        billingAddressService.deleteBillingAddress(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}
