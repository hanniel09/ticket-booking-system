package com.hanniel.ticketBookingSystem.services.ticket;

import com.hanniel.ticketBookingSystem.exceptions.global.BusinessRuleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class TicketInventoryRedisServiceTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private TicketInventoryRedisService inventoryRedisService;

    private UUID ticketTypeId;
    private String expectedKey;

    @BeforeEach
    void setUp() {
        ticketTypeId = UUID.randomUUID();
        expectedKey = "inventory:ticket-type:" + ticketTypeId;
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    @DisplayName("Deve inicializar estoque sem TTL na chave mestre")
    void shouldInitStockWithoutTtl() {
        inventoryRedisService.initStock(ticketTypeId, 100L);

        verify(valueOperations, times(1)).set(expectedKey, "100");
    }

    @Test
    @DisplayName("Deve decrementar estoque com sucesso quando houver saldo suficiente")
    void shouldReserveStockSuccessfullyWhenAvailable() {
        when(valueOperations.decrement(expectedKey, 2L)).thenReturn(8L);

        assertDoesNotThrow(() -> inventoryRedisService.reserveStock(ticketTypeId, 2L));

        verify(valueOperations, times(1)).decrement(expectedKey, 2L);
        verify(valueOperations, never()).increment(anyString(), anyLong());
    }

    @Test
    @DisplayName("Deve reverter o decremento e lancar excecao quando o saldo for insuficiente")
    void shouldCompensateAndThrowExceptionWhenStockIsInsufficient() {
        when(valueOperations.decrement(expectedKey, 5L)).thenReturn(-2L);

        assertThrows(BusinessRuleException.class, () ->
                inventoryRedisService.reserveStock(ticketTypeId, 5L)
        );

        verify(valueOperations, times(1)).decrement(expectedKey, 5L);
        verify(valueOperations, times(1)).increment(expectedKey, 5L);
    }

    @Test
    @DisplayName("Deve reverter o decremento e lancar excecao quando o retorno do decremento for nulo")
    void shouldCompensateAndThrowExceptionWhenDecrementReturnsNull() {
        when(valueOperations.decrement(expectedKey, 3L)).thenReturn(null);

        assertThrows(BusinessRuleException.class, () ->
                inventoryRedisService.reserveStock(ticketTypeId, 3L)
        );

        verify(valueOperations, times(1)).decrement(expectedKey, 3L);
        verify(valueOperations, times(1)).increment(expectedKey, 3L);
    }

    @Test
    @DisplayName("Deve incrementar estoque ao liberar reserva")
    void shouldReleaseStockSuccessfully() {
        inventoryRedisService.releaseStock(ticketTypeId, 4L);

        verify(valueOperations, times(1)).increment(expectedKey, 4L);
    }

    @Test
    @DisplayName("Deve criar reserva temporaria com TTL customizado")
    void shouldCreateReservationHoldWithCustomTtl() {
        UUID orderId = UUID.randomUUID();
        String expectedReservationKey = "reservation:order:" + orderId;
        Duration customTtl = Duration.ofMinutes(5);

        inventoryRedisService.createReservationHold(orderId, ticketTypeId, 2L, customTtl);

        String expectedPayload = String.format("{\"ticketTypeId\":\"%s\",\"quantity\":2}", ticketTypeId);
        verify(valueOperations, times(1)).set(expectedReservationKey, expectedPayload, customTtl);
    }

    @Test
    @DisplayName("Deve criar reserva temporaria com TTL padrao de 10 minutos quando TTL for nulo")
    void shouldCreateReservationHoldWithDefaultTtlWhenTtlIsNull() {
        UUID orderId = UUID.randomUUID();
        String expectedReservationKey = "reservation:order:" + orderId;

        inventoryRedisService.createReservationHold(orderId, ticketTypeId, 2L, null);

        String expectedPayload = String.format("{\"ticketTypeId\":\"%s\",\"quantity\":2}", ticketTypeId);
        verify(valueOperations, times(1)).set(expectedReservationKey, expectedPayload, Duration.ofMinutes(10));
    }

    @Test
    @DisplayName("Deve deletar a reserva temporaria no Redis")
    void shouldDeleteReservationHoldSuccessfully() {
        UUID orderId = UUID.randomUUID();
        String expectedReservationKey = "reservation:order:" + orderId;

        inventoryRedisService.deleteReservationHold(orderId);

        verify(redisTemplate, times(1)).delete(expectedReservationKey);
    }

    @Test
    @DisplayName("Deve deletar a chave de estoque mestre no Redis")
    void shouldDeleteStockSuccessfully() {
        inventoryRedisService.deleteStock(ticketTypeId);

        verify(redisTemplate, times(1)).delete(expectedKey);
    }
}
