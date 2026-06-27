package com.hanniel.ticketBookingSystem.services.billingAddress;

import com.hanniel.ticketBookingSystem.domain.billingAddress.BillingAddress;
import com.hanniel.ticketBookingSystem.domain.user.User;
import com.hanniel.ticketBookingSystem.dtos.billingAddress.BillingAddressRequestDTO;
import com.hanniel.ticketBookingSystem.dtos.billingAddress.BillingAddressResponseDTO;
import com.hanniel.ticketBookingSystem.exceptions.global.ResourceNotFoundException;
import com.hanniel.ticketBookingSystem.mappers.billingAddress.BillingAddressMapper;
import com.hanniel.ticketBookingSystem.repositories.billingAddress.BillingAddressRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BillingAddressServiceTest {

    @Mock
    private BillingAddressRepository billingAddressRepository;

    @Mock
    private BillingAddressMapper billingAddressMapper;

    @InjectMocks
    private BillingAddressService billingAddressService;

    @Test
    void createBillingAddress_Success() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("test@example.com");

        BillingAddressRequestDTO request = new BillingAddressRequestDTO("John Doe", "12345678901", "12345-678", "Street", "123", "", "Neighborhood", "City", "SP", "11999999999", "email@example.com", false);
        BillingAddress billingAddress = new BillingAddress(null, null, "John Doe", "12345678901", "12345-678", "Street", "123", "", "Neighborhood", "City", "SP", "11999999999", "email@example.com", false);
        BillingAddress saved = new BillingAddress(UUID.randomUUID(), user, "John Doe", "12345678901", "12345-678", "Street", "123", "", "Neighborhood", "City", "SP", "11999999999", "email@example.com", false);
        BillingAddressResponseDTO expectedResponse = new BillingAddressResponseDTO(saved.getId(), user.getId(), "John Doe", "123******01", "12345-678", "Street", "123", "", "Neighborhood", "City", "SP", "+11*****", "e***l@example.com", false);

        when(billingAddressMapper.toEntity(any(BillingAddressRequestDTO.class))).thenReturn(billingAddress);
        when(billingAddressRepository.save(any(BillingAddress.class))).thenReturn(saved);
        when(billingAddressMapper.toResponse(any(BillingAddress.class))).thenReturn(expectedResponse);

        BillingAddressResponseDTO response = billingAddressService.createBillingAddress(request, user);

        assertNotNull(response);
        assertEquals(saved.getId(), response.id());
        assertEquals(user.getId(), response.userId());
        verify(billingAddressRepository, times(1)).save(any(BillingAddress.class));
    }

    @Test
    void getUserBillingAddresses_Success() {
        User user = new User();
        user.setId(UUID.randomUUID());

        BillingAddress billingAddress = new BillingAddress(UUID.randomUUID(), user, "John", "123", "123", "Street", "12", "", "N", "C", "S", "123", "e@e.com", false);
        BillingAddressResponseDTO expectedResponse = new BillingAddressResponseDTO(billingAddress.getId(), user.getId(), "John", "***", "123", "Street", "12", "", "N", "C", "S", "***", "***", false);

        when(billingAddressRepository.findByUserId(user.getId())).thenReturn(List.of(billingAddress));
        when(billingAddressMapper.toResponse(any(BillingAddress.class))).thenReturn(expectedResponse);

        List<BillingAddressResponseDTO> response = billingAddressService.getUserBillingAddresses(user);

        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
    }

    @Test
    void getBillingAddressById_Success() {
        User user = new User();
        user.setId(UUID.randomUUID());

        UUID addressId = UUID.randomUUID();
        BillingAddress billingAddress = new BillingAddress(addressId, user, "John", "123", "123", "Street", "12", "", "N", "C", "S", "123", "e@e.com", false);
        BillingAddressResponseDTO expectedResponse = new BillingAddressResponseDTO(addressId, user.getId(), "John", "***", "123", "Street", "12", "", "N", "C", "S", "***", "***", false);

        when(billingAddressRepository.findById(addressId)).thenReturn(Optional.of(billingAddress));
        when(billingAddressMapper.toResponse(any(BillingAddress.class))).thenReturn(expectedResponse);

        BillingAddressResponseDTO response = billingAddressService.getBillingAddressById(addressId, user);

        assertNotNull(response);
        assertEquals(addressId, response.id());
    }

    @Test
    void getBillingAddressById_AccessDenied_ThrowsException() {
        User owner = new User();
        owner.setId(UUID.randomUUID());

        User differentUser = new User();
        differentUser.setId(UUID.randomUUID());

        UUID addressId = UUID.randomUUID();
        BillingAddress billingAddress = new BillingAddress(addressId, owner, "John", "123", "123", "Street", "12", "", "N", "C", "S", "123", "e@e.com", false);

        when(billingAddressRepository.findById(addressId)).thenReturn(Optional.of(billingAddress));

        assertThrows(ResourceNotFoundException.class, () -> billingAddressService.getBillingAddressById(addressId, differentUser));
    }
}
