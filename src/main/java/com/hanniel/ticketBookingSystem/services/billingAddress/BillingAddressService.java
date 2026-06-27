package com.hanniel.ticketBookingSystem.services.billingAddress;

import com.hanniel.ticketBookingSystem.domain.billingAddress.BillingAddress;
import com.hanniel.ticketBookingSystem.domain.user.User;
import com.hanniel.ticketBookingSystem.dtos.billingAddress.BillingAddressRequestDTO;
import com.hanniel.ticketBookingSystem.dtos.billingAddress.BillingAddressResponseDTO;
import com.hanniel.ticketBookingSystem.exceptions.global.ResourceNotFoundException;
import com.hanniel.ticketBookingSystem.mappers.billingAddress.BillingAddressMapper;
import com.hanniel.ticketBookingSystem.repositories.billingAddress.BillingAddressRepository;
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
public class BillingAddressService {

    private final BillingAddressRepository billingAddressRepository;
    private final BillingAddressMapper billingAddressMapper;

    @Transactional
    public BillingAddressResponseDTO createBillingAddress(BillingAddressRequestDTO request, User currentUser) {
        log.info("Creating billing address for user: {}", currentUser.getEmail());
        BillingAddress billingAddress = billingAddressMapper.toEntity(request);
        billingAddress.setUser(currentUser);

        BillingAddress saved = billingAddressRepository.save(billingAddress);
        log.info("Billing address created successfully with ID: {} for user ID: {}", saved.getId(), currentUser.getId());
        return billingAddressMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<BillingAddressResponseDTO> getUserBillingAddresses(User currentUser) {
        log.info("Retrieving all billing addresses for user: {}", currentUser.getEmail());
        return billingAddressRepository.findByUserId(currentUser.getId()).stream()
                .map(billingAddressMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BillingAddressResponseDTO getBillingAddressById(UUID id, User currentUser) {
        log.info("Retrieving billing address with ID: {} for user: {}", id, currentUser.getEmail());
        BillingAddress billingAddress = billingAddressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Billing address not found"));

        validateOwnership(billingAddress, currentUser);

        return billingAddressMapper.toResponse(billingAddress);
    }

    @Transactional
    public BillingAddressResponseDTO updateBillingAddress(UUID id, BillingAddressRequestDTO request, User currentUser) {
        log.info("Updating billing address with ID: {} for user: {}", id, currentUser.getEmail());
        BillingAddress billingAddress = billingAddressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Billing address not found"));

        validateOwnership(billingAddress, currentUser);

        billingAddressMapper.updateEntityFromRequest(request, billingAddress);
        BillingAddress updated = billingAddressRepository.save(billingAddress);
        log.info("Billing address updated successfully with ID: {}", updated.getId());
        return billingAddressMapper.toResponse(updated);
    }

    @Transactional
    public void deleteBillingAddress(UUID id, User currentUser) {
        log.info("Deleting billing address with ID: {} for user: {}", id, currentUser.getEmail());
        BillingAddress billingAddress = billingAddressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Billing address not found"));

        validateOwnership(billingAddress, currentUser);

        billingAddressRepository.delete(billingAddress);
        log.info("Billing address deleted successfully with ID: {}", id);
    }

    private void validateOwnership(BillingAddress billingAddress, User currentUser) {
        if (billingAddress.getUser() == null || !billingAddress.getUser().getId().equals(currentUser.getId())) {
            log.warn("Access denied for user {} to billing address ID: {}", currentUser.getEmail(), billingAddress.getId());
            throw new ResourceNotFoundException("Billing address not found");
        }
    }
}
