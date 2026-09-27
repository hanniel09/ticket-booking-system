package com.hanniel.ticketBookingSystem.services.order;

import com.hanniel.ticketBookingSystem.domain.billingAddress.BillingAddress;
import com.hanniel.ticketBookingSystem.domain.order.Order;
import com.hanniel.ticketBookingSystem.domain.order.enums.OrderStatus;
import com.hanniel.ticketBookingSystem.domain.ticket.TicketType;
import com.hanniel.ticketBookingSystem.domain.user.User;
import com.hanniel.ticketBookingSystem.dtos.order.OrderRequestDTO;
import com.hanniel.ticketBookingSystem.dtos.order.OrderResponseDTO;
import com.hanniel.ticketBookingSystem.exceptions.global.BusinessRuleException;
import com.hanniel.ticketBookingSystem.exceptions.global.ResourceNotFoundException;
import com.hanniel.ticketBookingSystem.mappers.order.OrderMapper;
import com.hanniel.ticketBookingSystem.repositories.billingAddress.BillingAddressRepository;
import com.hanniel.ticketBookingSystem.repositories.order.OrderRepository;
import com.hanniel.ticketBookingSystem.repositories.ticket.TicketTypeRepository;
import com.hanniel.ticketBookingSystem.repositories.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
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
    void createOrder_Success_CalculatesTotalAmountAndSetsPendingStatus() {
        UUID userId = UUID.randomUUID();
        UUID ticketTypeId = UUID.randomUUID();
        UUID billingAddressId = UUID.randomUUID();
        OrderRequestDTO request = new OrderRequestDTO(userId, ticketTypeId, 3, null, null, billingAddressId);

        User user = new User();
        user.setId(userId);

        TicketType ticketType = new TicketType();
        ticketType.setId(ticketTypeId);
        ticketType.setPrice(new BigDecimal("150.00"));
        ticketType.setQuantityAvailable(10L);

        BillingAddress billingAddress = new BillingAddress();
        billingAddress.setId(billingAddressId);
        billingAddress.setUser(user);

        Order initialOrder = new Order();
        Order savedOrder = new Order(UUID.randomUUID(), user, ticketType, 3, new BigDecimal("450.00"),
                OrderStatus.PENDING, billingAddress, OffsetDateTime.now());
        OrderResponseDTO expectedResponse = new OrderResponseDTO(savedOrder.getId(), userId, ticketTypeId, 3,
                new BigDecimal("450.00"), OrderStatus.PENDING, billingAddressId, savedOrder.getCreatedAt());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(ticketTypeRepository.findById(ticketTypeId)).thenReturn(Optional.of(ticketType));
        when(billingAddressRepository.findById(billingAddressId)).thenReturn(Optional.of(billingAddress));
        when(orderMapper.toEntity(any(OrderRequestDTO.class))).thenReturn(initialOrder);
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(orderMapper.toResponse(any(Order.class))).thenReturn(expectedResponse);

        OrderResponseDTO response = orderService.createOrder(request);

        assertNotNull(response);
        assertEquals(expectedResponse.id(), response.id());
        assertEquals(expectedResponse.totalAmount(), response.totalAmount());
        assertEquals(OrderStatus.PENDING, response.status());

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        Order captured = orderCaptor.getValue();
        assertEquals(new BigDecimal("450.00"), captured.getTotalAmount());
        assertEquals(OrderStatus.PENDING, captured.getStatus());
        assertEquals(3, captured.getQuantity());
        assertEquals(user, captured.getUser());
        assertEquals(ticketType, captured.getTicketType());
        assertEquals(billingAddress, captured.getBillingAddress());
    }

    @Test
    void createOrder_InsufficientStock_ThrowsBusinessRuleException() {
        UUID userId = UUID.randomUUID();
        UUID ticketTypeId = UUID.randomUUID();
        UUID billingAddressId = UUID.randomUUID();
        OrderRequestDTO request = new OrderRequestDTO(userId, ticketTypeId, 5, null, null, billingAddressId);

        User user = new User();
        user.setId(userId);

        TicketType ticketType = new TicketType();
        ticketType.setId(ticketTypeId);
        ticketType.setPrice(new BigDecimal("100.00"));
        ticketType.setQuantityAvailable(2L);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(ticketTypeRepository.findById(ticketTypeId)).thenReturn(Optional.of(ticketType));

        BusinessRuleException exception = assertThrows(BusinessRuleException.class,
                () -> orderService.createOrder(request));
        assertEquals("Estoque insuficiente", exception.getMessage());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void createOrder_StockNull_ThrowsBusinessRuleException() {
        UUID userId = UUID.randomUUID();
        UUID ticketTypeId = UUID.randomUUID();
        UUID billingAddressId = UUID.randomUUID();
        OrderRequestDTO request = new OrderRequestDTO(userId, ticketTypeId, 1, null, null, billingAddressId);

        User user = new User();
        user.setId(userId);

        TicketType ticketType = new TicketType();
        ticketType.setId(ticketTypeId);
        ticketType.setPrice(new BigDecimal("100.00"));
        ticketType.setQuantityAvailable(null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(ticketTypeRepository.findById(ticketTypeId)).thenReturn(Optional.of(ticketType));

        BusinessRuleException exception = assertThrows(BusinessRuleException.class,
                () -> orderService.createOrder(request));
        assertEquals("Estoque insuficiente", exception.getMessage());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void createOrder_NullBillingAddressId_ThrowsBusinessRuleException() {
        UUID userId = UUID.randomUUID();
        UUID ticketTypeId = UUID.randomUUID();
        OrderRequestDTO request = new OrderRequestDTO(userId, ticketTypeId, 1, null, null, null);

        User user = new User();
        user.setId(userId);

        TicketType ticketType = new TicketType();
        ticketType.setId(ticketTypeId);
        ticketType.setPrice(new BigDecimal("100.00"));
        ticketType.setQuantityAvailable(10L);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(ticketTypeRepository.findById(ticketTypeId)).thenReturn(Optional.of(ticketType));

        BusinessRuleException exception = assertThrows(BusinessRuleException.class,
                () -> orderService.createOrder(request));
        assertEquals("Endereço de cobrança é obrigatório", exception.getMessage());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void createOrder_UserNotFound_ThrowsException() {
        UUID userId = UUID.randomUUID();
        UUID ticketTypeId = UUID.randomUUID();
        OrderRequestDTO request = new OrderRequestDTO(userId, ticketTypeId, 2, null, null, UUID.randomUUID());

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.createOrder(request));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void createOrder_TicketTypeNotFound_ThrowsException() {
        UUID userId = UUID.randomUUID();
        UUID ticketTypeId = UUID.randomUUID();
        OrderRequestDTO request = new OrderRequestDTO(userId, ticketTypeId, 2, null, null, UUID.randomUUID());

        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(ticketTypeRepository.findById(ticketTypeId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.createOrder(request));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void createOrder_BillingAddressNotFound_ThrowsResourceNotFoundException() {
        UUID userId = UUID.randomUUID();
        UUID ticketTypeId = UUID.randomUUID();
        UUID billingAddressId = UUID.randomUUID();
        OrderRequestDTO request = new OrderRequestDTO(userId, ticketTypeId, 2, null, null, billingAddressId);

        User user = new User();
        user.setId(userId);

        TicketType ticketType = new TicketType();
        ticketType.setId(ticketTypeId);
        ticketType.setPrice(new BigDecimal("100.00"));
        ticketType.setQuantityAvailable(10L);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(ticketTypeRepository.findById(ticketTypeId)).thenReturn(Optional.of(ticketType));
        when(billingAddressRepository.findById(billingAddressId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.createOrder(request));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void createOrder_BillingAddressNotOwnedByUser_ThrowsBusinessRuleException() {
        UUID userId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();
        UUID ticketTypeId = UUID.randomUUID();
        UUID billingAddressId = UUID.randomUUID();
        OrderRequestDTO request = new OrderRequestDTO(userId, ticketTypeId, 2, null, null, billingAddressId);

        User user = new User();
        user.setId(userId);

        User otherUser = new User();
        otherUser.setId(otherUserId);

        TicketType ticketType = new TicketType();
        ticketType.setId(ticketTypeId);
        ticketType.setPrice(new BigDecimal("100.00"));
        ticketType.setQuantityAvailable(10L);

        BillingAddress billingAddress = new BillingAddress();
        billingAddress.setId(billingAddressId);
        billingAddress.setUser(otherUser);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(ticketTypeRepository.findById(ticketTypeId)).thenReturn(Optional.of(ticketType));
        when(billingAddressRepository.findById(billingAddressId)).thenReturn(Optional.of(billingAddress));

        BusinessRuleException exception = assertThrows(BusinessRuleException.class,
                () -> orderService.createOrder(request));
        assertEquals("Endereço de cobrança não pertence ao usuário", exception.getMessage());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void getAllOrders_Success() {
        Order order = new Order();
        OrderResponseDTO responseDTO = new OrderResponseDTO(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                1, BigDecimal.TEN, OrderStatus.PENDING, UUID.randomUUID(), OffsetDateTime.now());

        when(orderRepository.findAll()).thenReturn(List.of(order));
        when(orderMapper.toResponse(order)).thenReturn(responseDTO);

        List<OrderResponseDTO> result = orderService.getAllOrders();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(responseDTO, result.get(0));
    }

    @Test
    void getOrderById_Success() {
        UUID id = UUID.randomUUID();
        Order order = new Order();
        OrderResponseDTO responseDTO = new OrderResponseDTO(id, UUID.randomUUID(), UUID.randomUUID(),
                1, BigDecimal.TEN, OrderStatus.PENDING, UUID.randomUUID(), OffsetDateTime.now());

        when(orderRepository.findById(id)).thenReturn(Optional.of(order));
        when(orderMapper.toResponse(order)).thenReturn(responseDTO);

        OrderResponseDTO result = orderService.getOrderById(id);

        assertNotNull(result);
        assertEquals(id, result.id());
    }

    @Test
    void getOrderById_NotFound_ThrowsException() {
        UUID id = UUID.randomUUID();
        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(id));
    }

    @Test
    void updateOrder_Success() {
        UUID orderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID ticketTypeId = UUID.randomUUID();
        UUID billingAddressId = UUID.randomUUID();
        OrderRequestDTO request = new OrderRequestDTO(userId, ticketTypeId, 2, new BigDecimal("200.00"),
                OrderStatus.PENDING, billingAddressId);

        User user = new User();
        user.setId(userId);

        TicketType ticketType = new TicketType();
        ticketType.setId(ticketTypeId);

        BillingAddress billingAddress = new BillingAddress();
        billingAddress.setId(billingAddressId);
        billingAddress.setUser(user);

        Order order = new Order();
        order.setId(orderId);

        Order saved = new Order(orderId, user, ticketType, 2, new BigDecimal("200.00"), OrderStatus.PENDING,
                billingAddress, OffsetDateTime.now());
        OrderResponseDTO expectedResponse = new OrderResponseDTO(orderId, userId, ticketTypeId, 2,
                new BigDecimal("200.00"), OrderStatus.PENDING, billingAddressId, saved.getCreatedAt());

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(ticketTypeRepository.findById(ticketTypeId)).thenReturn(Optional.of(ticketType));
        when(billingAddressRepository.findById(billingAddressId)).thenReturn(Optional.of(billingAddress));
        when(orderRepository.save(order)).thenReturn(saved);
        when(orderMapper.toResponse(saved)).thenReturn(expectedResponse);

        OrderResponseDTO response = orderService.updateOrder(orderId, request);

        assertNotNull(response);
        assertEquals(orderId, response.id());
        verify(orderRepository).save(order);
    }

    @Test
    void updateOrder_BillingAddressNotOwnedByUser_ThrowsBusinessRuleException() {
        UUID orderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();
        UUID ticketTypeId = UUID.randomUUID();
        UUID billingAddressId = UUID.randomUUID();
        OrderRequestDTO request = new OrderRequestDTO(userId, ticketTypeId, 2, new BigDecimal("200.00"),
                OrderStatus.PENDING, billingAddressId);

        User user = new User();
        user.setId(userId);

        User otherUser = new User();
        otherUser.setId(otherUserId);

        TicketType ticketType = new TicketType();
        ticketType.setId(ticketTypeId);

        BillingAddress billingAddress = new BillingAddress();
        billingAddress.setId(billingAddressId);
        billingAddress.setUser(otherUser);

        Order order = new Order();
        order.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(ticketTypeRepository.findById(ticketTypeId)).thenReturn(Optional.of(ticketType));
        when(billingAddressRepository.findById(billingAddressId)).thenReturn(Optional.of(billingAddress));

        BusinessRuleException exception = assertThrows(BusinessRuleException.class,
                () -> orderService.updateOrder(orderId, request));
        assertEquals("Endereço de cobrança não pertence ao usuário", exception.getMessage());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void deleteOrder_Success() {
        UUID id = UUID.randomUUID();
        when(orderRepository.existsById(id)).thenReturn(true);

        orderService.deleteOrder(id);

        verify(orderRepository).deleteById(id);
    }

    @Test
    void deleteOrder_NotFound_ThrowsException() {
        UUID id = UUID.randomUUID();
        when(orderRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> orderService.deleteOrder(id));
        verify(orderRepository, never()).deleteById(any(UUID.class));
    }
}
