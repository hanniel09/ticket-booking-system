package com.hanniel.ticketBookingSystem.controllers.order;

import com.hanniel.ticketBookingSystem.config.security.SecurityFilter;
import com.hanniel.ticketBookingSystem.config.security.TokenService;
import com.hanniel.ticketBookingSystem.domain.order.enums.OrderStatus;
import com.hanniel.ticketBookingSystem.dtos.order.OrderRequestDTO;
import com.hanniel.ticketBookingSystem.dtos.order.OrderResponseDTO;
import com.hanniel.ticketBookingSystem.repositories.user.UserRepository;
import com.hanniel.ticketBookingSystem.services.order.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private SecurityFilter securityFilter;

    @Test
    void createOrder_Success() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID ticketTypeId = UUID.randomUUID();
        UUID billingAddressId = UUID.randomUUID();
        OrderResponseDTO response = new OrderResponseDTO(UUID.randomUUID(), userId, ticketTypeId, 2,
                new BigDecimal("200.00"), OrderStatus.PENDING, billingAddressId, OffsetDateTime.now());

        when(orderService.createOrder(any(OrderRequestDTO.class))).thenReturn(response);

        String json = String.format("{\"userId\":\"%s\",\"ticketTypeId\":\"%s\",\"quantity\":2,\"totalAmount\":200.00,\"status\":\"PENDING\",\"billingAddressId\":\"%s\"}",
                userId, ticketTypeId, billingAddressId);

        mockMvc.perform(post("/orders")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.billingAddressId").value(billingAddressId.toString()));

        verify(orderService, times(1)).createOrder(any(OrderRequestDTO.class));
    }

    @Test
    void createOrder_MissingBillingAddress_ReturnsBadRequest() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID ticketTypeId = UUID.randomUUID();

        String json = String.format("{\"userId\":\"%s\",\"ticketTypeId\":\"%s\",\"quantity\":2,\"totalAmount\":200.00,\"status\":\"PENDING\",\"billingAddressId\":null}",
                userId, ticketTypeId);

        mockMvc.perform(post("/orders")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verify(orderService, never()).createOrder(any(OrderRequestDTO.class));
    }

    @Test
    void createOrder_InvalidQuantity_ReturnsBadRequest() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID ticketTypeId = UUID.randomUUID();
        UUID billingAddressId = UUID.randomUUID();

        String json = String.format("{\"userId\":\"%s\",\"ticketTypeId\":\"%s\",\"quantity\":0,\"totalAmount\":200.00,\"status\":\"PENDING\",\"billingAddressId\":\"%s\"}",
                userId, ticketTypeId, billingAddressId);

        mockMvc.perform(post("/orders")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verify(orderService, never()).createOrder(any(OrderRequestDTO.class));
    }

    @Test
    void getAllOrders_Success() throws Exception {
        OrderResponseDTO response = new OrderResponseDTO(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 2,
                new BigDecimal("200.00"), OrderStatus.PENDING, null, OffsetDateTime.now());
        when(orderService.getAllOrders()).thenReturn(List.of(response));

        mockMvc.perform(get("/orders")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].quantity").value(2));

        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    void getOrderById_Success() throws Exception {
        UUID id = UUID.randomUUID();
        OrderResponseDTO response = new OrderResponseDTO(id, UUID.randomUUID(), UUID.randomUUID(), 2,
                new BigDecimal("200.00"), OrderStatus.PENDING, null, OffsetDateTime.now());
        when(orderService.getOrderById(id)).thenReturn(response);

        mockMvc.perform(get("/orders/{id}", id)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));

        verify(orderService, times(1)).getOrderById(id);
    }

    @Test
    void updateOrder_Success() throws Exception {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID ticketTypeId = UUID.randomUUID();
        UUID billingAddressId = UUID.randomUUID();
        OrderResponseDTO response = new OrderResponseDTO(id, userId, ticketTypeId, 2,
                new BigDecimal("200.00"), OrderStatus.PENDING, billingAddressId, OffsetDateTime.now());

        when(orderService.updateOrder(eq(id), any(OrderRequestDTO.class))).thenReturn(response);

        String json = String.format("{\"userId\":\"%s\",\"ticketTypeId\":\"%s\",\"quantity\":2,\"totalAmount\":200.00,\"status\":\"PENDING\",\"billingAddressId\":\"%s\"}",
                userId, ticketTypeId, billingAddressId);

        mockMvc.perform(put("/orders/{id}", id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));

        verify(orderService, times(1)).updateOrder(eq(id), any(OrderRequestDTO.class));
    }

    @Test
    void deleteOrder_Success() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(orderService).deleteOrder(id);

        mockMvc.perform(delete("/orders/{id}", id)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(orderService, times(1)).deleteOrder(id);
    }
}
