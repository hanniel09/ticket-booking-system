package com.hanniel.ticketBookingSystem.controllers.billingAddress;

import com.hanniel.ticketBookingSystem.config.security.SecurityFilter;
import com.hanniel.ticketBookingSystem.config.security.TokenService;
import com.hanniel.ticketBookingSystem.dtos.billingAddress.BillingAddressRequestDTO;
import com.hanniel.ticketBookingSystem.dtos.billingAddress.BillingAddressResponseDTO;
import com.hanniel.ticketBookingSystem.repositories.user.UserRepository;
import com.hanniel.ticketBookingSystem.services.billingAddress.BillingAddressService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BillingAddressController.class)
class BillingAddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BillingAddressService billingAddressService;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private SecurityFilter securityFilter;

    @Test
    void createBillingAddress_Success() throws Exception {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BillingAddressResponseDTO response = new BillingAddressResponseDTO(id, userId, "John Doe", "123******01", "12345-678", "Street", "123", "", "Neighborhood", "City", "SP", "+11*****", "e***l@example.com", false);

        when(billingAddressService.createBillingAddress(any(BillingAddressRequestDTO.class), any())).thenReturn(response);

        String json = "{\"name\":\"John Doe\",\"taxId\":\"12345678901\",\"postalCode\":\"12345-678\",\"street\":\"Street\",\"number\":\"123\",\"complement\":\"\",\"neighborhood\":\"Neighborhood\",\"city\":\"City\",\"uf\":\"SP\",\"phone\":\"11999999999\",\"email\":\"email@example.com\",\"shipping\":false}";

        mockMvc.perform(post("/billing-addresses")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.taxId").value("123******01"));

        verify(billingAddressService, times(1)).createBillingAddress(any(BillingAddressRequestDTO.class), any());
    }

    @Test
    void getUserBillingAddresses_Success() throws Exception {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BillingAddressResponseDTO response = new BillingAddressResponseDTO(id, userId, "John Doe", "123******01", "12345-678", "Street", "123", "", "Neighborhood", "City", "SP", "+11*****", "e***l@example.com", false);

        when(billingAddressService.getUserBillingAddresses(any())).thenReturn(List.of(response));

        mockMvc.perform(get("/billing-addresses")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].taxId").value("123******01"));

        verify(billingAddressService, times(1)).getUserBillingAddresses(any());
    }
}
