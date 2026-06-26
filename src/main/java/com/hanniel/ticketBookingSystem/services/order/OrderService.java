package com.hanniel.ticketBookingSystem.services.order;

import com.hanniel.ticketBookingSystem.domain.billingAddress.BillingAddress;
import com.hanniel.ticketBookingSystem.domain.order.Order;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final BillingAddressRepository billingAddressRepository;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO request) {
        log.info("Creating order for user ID: {} and ticket type ID: {}", request.userId(), request.ticketTypeId());

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + request.userId()));

        TicketType ticketType = ticketTypeRepository.findById(request.ticketTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Ticket type not found with ID: " + request.ticketTypeId()));

        Order order = orderMapper.toEntity(request);
        order.setUser(user);
        order.setTicketType(ticketType);
        order.setCreatedAt(OffsetDateTime.now());

        if (request.billingAddressId() != null) {
            BillingAddress billingAddress = billingAddressRepository.findById(request.billingAddressId())
                    .orElseThrow(() -> new ResourceNotFoundException("Billing address not found with ID: " + request.billingAddressId()));
            order.setBillingAddress(billingAddress);
        }

        Order saved = orderRepository.save(order);
        log.info("Order created successfully with ID: {}", saved.getId());
        return orderMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getAllOrders() {
        log.info("Retrieving all orders");
        return orderRepository.findAll().stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderResponseDTO getOrderById(UUID id) {
        log.info("Retrieving order with ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
        return orderMapper.toResponse(order);
    }

    @Transactional
    public OrderResponseDTO updateOrder(UUID id, OrderRequestDTO request) {
        log.info("Updating order with ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + request.userId()));

        TicketType ticketType = ticketTypeRepository.findById(request.ticketTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Ticket type not found with ID: " + request.ticketTypeId()));

        orderMapper.updateEntityFromRequest(request, order);
        order.setUser(user);
        order.setTicketType(ticketType);

        if (request.billingAddressId() != null) {
            BillingAddress billingAddress = billingAddressRepository.findById(request.billingAddressId())
                    .orElseThrow(() -> new ResourceNotFoundException("Billing address not found with ID: " + request.billingAddressId()));
            order.setBillingAddress(billingAddress);
        } else {
            order.setBillingAddress(null);
        }

        Order updated = orderRepository.save(order);
        log.info("Order updated successfully with ID: {}", updated.getId());
        return orderMapper.toResponse(updated);
    }

    @Transactional
    public void deleteOrder(UUID id) {
        log.info("Deleting order with ID: {}", id);
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order not found with ID: " + id);
        }
        orderRepository.deleteById(id);
        log.info("Order deleted successfully with ID: {}", id);
    }
}
