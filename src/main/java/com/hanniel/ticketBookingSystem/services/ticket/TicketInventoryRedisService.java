package com.hanniel.ticketBookingSystem.services.ticket;

import com.hanniel.ticketBookingSystem.exceptions.global.BusinessRuleException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Service
public class TicketInventoryRedisService {

    private static final String KEY_PREFIX = "inventory:ticket-type:";
    private static final String RESERVATION_KEY_PREFIX = "reservation:order:";
    private static final Duration DEFAULT_HOLD_TTL = Duration.ofMinutes(10);

    private final StringRedisTemplate redisTemplate;

    public TicketInventoryRedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void initStock(UUID ticketTypeId, Long initialQuantity) {
        log.info("Initializing stock for ticket type ID: {} with quantity: {}", ticketTypeId, initialQuantity);
        String key = buildKey(ticketTypeId);
        redisTemplate.opsForValue().set(key, String.valueOf(initialQuantity));
        log.info("Stock initialized successfully for key: {}", key);
    }

    public void reserveStock(UUID ticketTypeId, Long quantity) {
        log.info("Reserving stock for ticket type ID: {} with quantity: {}", ticketTypeId, quantity);
        String key = buildKey(ticketTypeId);

        Long remainingStock = redisTemplate.opsForValue().decrement(key, quantity);

        if (remainingStock == null || remainingStock < 0) {
            log.warn("Stock insufficient for ticket type ID: {}. Rolling back decrement.", ticketTypeId);
            redisTemplate.opsForValue().increment(key, quantity);
            throw new BusinessRuleException("Ingressos esgotados no lote");
        }
        log.info("Stock reserved successfully for key: {}. Remaining stock: {}", key, remainingStock);
    }

    public void releaseStock(UUID ticketTypeId, Long quantity) {
        log.info("Releasing stock for ticket type ID: {} with quantity: {}", ticketTypeId, quantity);
        String key = buildKey(ticketTypeId);
        redisTemplate.opsForValue().increment(key, quantity);
        log.info("Stock released successfully for key: {}", key);
    }

    public void createReservationHold(UUID orderId, UUID ticketTypeId, Long quantity, Duration ttl) {
        log.info("Creating reservation hold for order ID: {}, ticket type ID: {}, quantity: {}", orderId, ticketTypeId, quantity);
        String key = buildReservationKey(orderId);
        String payload = String.format("{\"ticketTypeId\":\"%s\",\"quantity\":%d}", ticketTypeId, quantity);
        Duration holdTtl = ttl != null ? ttl : DEFAULT_HOLD_TTL;
        redisTemplate.opsForValue().set(key, payload, holdTtl);
        log.info("Reservation hold created successfully for key: {} with TTL: {}s", key, holdTtl.toSeconds());
    }

    public void deleteReservationHold(UUID orderId) {
        log.info("Deleting reservation hold for order ID: {}", orderId);
        String key = buildReservationKey(orderId);
        redisTemplate.delete(key);
        log.info("Reservation hold deleted successfully for key: {}", key);
    }

    public void deleteStock(UUID ticketTypeId) {
        log.info("Deleting stock for ticket type ID: {}", ticketTypeId);
        String key = buildKey(ticketTypeId);
        redisTemplate.delete(key);
        log.info("Stock deleted successfully for key: {}", key);
    }

    private String buildKey(UUID ticketTypeId) {
        return KEY_PREFIX + ticketTypeId.toString();
    }

    private String buildReservationKey(UUID orderId) {
        return RESERVATION_KEY_PREFIX + orderId.toString();
    }
}
