package com.hanniel.ticketBookingSystem.repositories.ticket;

import com.hanniel.ticketBookingSystem.domain.ticket.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TicketTypeRepository extends JpaRepository<TicketType, UUID> {
}
