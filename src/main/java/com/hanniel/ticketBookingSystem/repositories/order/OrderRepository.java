package com.hanniel.ticketBookingSystem.repositories.order;

import com.hanniel.ticketBookingSystem.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
