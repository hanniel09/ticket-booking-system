package com.hanniel.ticketBookingSystem.services.order;

import com.hanniel.ticketBookingSystem.domain.billingAddress.BillingAddress;
import com.hanniel.ticketBookingSystem.domain.order.Order;
import com.hanniel.ticketBookingSystem.domain.order.enums.OrderStatus;
import com.hanniel.ticketBookingSystem.domain.ticket.TicketType;
import com.hanniel.ticketBookingSystem.domain.user.User;
import com.hanniel.ticketBookingSystem.dtos.order.OrderRequestDTO;
import com.hanniel.ticketBookingSystem.dtos.order.OrderResponseDTO;
import com.hanniel.ticketBookingSystem.exceptions.global.ResourceNotFoundException;
import com.hanniel.ticketBookingSystem.mappers.order.OrderMapper;
import com.hanniel.ticketBookingSystem.repositories.billingAddress.BillingAddressRepository;
import com.hanniel.ticketBookingSystem.repositories.order.OrderRepository;
import com.hanniel.ticketBookingSystem.repositories.ticket.TicketTypeRepository;
import com.hanniel.ticketBookingSystem.repositories.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TicketTypeRepository ticketTypeRepository;

    @Mock
    private BillingAddressRepository billingAddressRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder_Success() {
        UUID userId = UUID.randomUUID();
        UUID ticketTypeId = UUID.randomUUID();
        OrderRequestDTO request = new OrderRequestDTO(userId, ticketTypeId, 2, new BigDecimal("200.00"), OrderStatus.PENDING, null);

        User user = new User();
        user.setId(userId);

        TicketType ticketType = new TicketType();
        ticketType.setId(ticketTypeId);

        Order order = new Order(null, user, ticketType, request.quantity(), request.totalAmount(), request.status(), null, null);
        Order saved = new Order(UUID.randomUUID(), user, ticketType, request.quantity(), request.totalAmount(), request.status(), null, OffsetDateTime.now());
        OrderResponseDTO expectedResponse = new OrderResponseDTO(saved.getId(), userId, ticketTypeId, 2, new BigDecimal("200.00"), OrderStatus.PENDING, null, saved.getCreatedAt());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(ticketTypeRepository.findById(ticketTypeId)).thenReturn(Optional.of(ticketType));
        when(orderMapper.toEntity(any(OrderRequestDTO.class))).thenReturn(order);
        when(orderRepository.save(any(Order.class))).thenReturn(saved);
        when(orderMapper.toResponse(any(Order.class))).thenReturn(expectedResponse);

        OrderResponseDTO response = orderService.createOrder(request);

        assertNotNull(response);
        assertEquals(saved.getId(), response.id());
        assertEquals(userId, response.userId());
        assertEquals(ticketTypeId, response.ticketTypeId());
    }

    @Test
    void createOrder_UserNotFound_ThrowsException() {
        UUID userId = UUID.randomUUID();
        UUID ticketTypeId = UUID.randomUUID();
        OrderRequestDTO request = new OrderRequestDTO(userId, ticketTypeId, 2, new BigDecimal("200.00"), OrderStatus.PENDING, null);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.createOrder(request));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void createOrder_TicketTypeNotFound_ThrowsException() {
        UUID userId = UUID.randomUUID();
        UUID ticketTypeId = UUID.randomUUID();
        OrderRequestDTO request = new OrderRequestDTO(userId, ticketTypeId, 2, new BigDecimal("200.00"), OrderStatus.PENDING, null);

        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(ticketTypeRepository.findById(ticketTypeId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.createOrder(request));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void createOrder_WithBillingAddress_Success() {
        UUID userId = UUID.randomUUID();
        UUID ticketTypeId = UUID.randomUUID();
        UUID billingAddressId = UUID.randomUUID();
        OrderRequestDTO request = new OrderRequestDTO(userId, ticketTypeId, 2, new BigDecimal("200.00"), OrderStatus.PENDING, billingAddressId);

        User user = new User();
        user.setId(userId);

        TicketType ticketType = new TicketType();
        ticketType.setId(ticketTypeId);

        BillingAddress billingAddress = new BillingAddress();
        billingAddress.setId(billingAddressId);

        Order order = new Order(null, user, ticketType, request.quantity(), request.totalAmount(), request.status(), null, null);
        Order saved = new Order(UUID.randomUUID(), user, ticketType, request.quantity(), request.totalAmount(), request.status(), billingAddress, OffsetDateTime.now());
        OrderResponseDTO expectedResponse = new OrderResponseDTO(saved.getId(), userId, ticketTypeId, 2, new BigDecimal("200.00"), OrderStatus.PENDING, billingAddressId, saved.getCreatedAt());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(ticketTypeRepository.findById(ticketTypeId)).thenReturn(Optional.of(ticketType));
        when(billingAddressRepository.findById(billingAddressId)).thenReturn(Optional.of(billingAddress));
        when(orderMapper.toEntity(any(OrderRequestDTO.class))).thenReturn(order);
        when(orderRepository.save(any(Order.class))).thenReturn(saved);
        when(orderMapper.toResponse(any(Order.class))).thenReturn(expectedResponse);

        OrderResponseDTO response = orderService.createOrder(request);

        assertNotNull(response);
        assertEquals(billingAddressId, response.billingAddressId());
    }
}
