package com.hanniel.ticketBookingSystem.repositories.event;

import com.hanniel.ticketBookingSystem.domain.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
}
