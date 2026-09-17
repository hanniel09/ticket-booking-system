package com.hanniel.ticketBookingSystem.controllers.order;

import com.hanniel.ticketBookingSystem.dtos.order.OrderRequestDTO;
import com.hanniel.ticketBookingSystem.dtos.order.OrderResponseDTO;
import com.hanniel.ticketBookingSystem.services.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Checkout e simulação de pagamentos de ingressos")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Criar novo pedido", description = "Realiza o pedido de compra de ingressos para um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody @Valid OrderRequestDTO request) {
        log.info("Received request to create order for user ID: {} and ticket type ID: {}", request.userId(), request.ticketTypeId());
        OrderResponseDTO created = orderService.createOrder(request);
        log.info("Order created successfully with ID: {}", created.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Listar pedidos", description = "Retorna todos os pedidos registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        log.info("Received request to list all orders");
        List<OrderResponseDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @Operation(summary = "Buscar pedido por ID", description = "Retorna as informações de um pedido específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido retornado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable UUID id) {
        log.info("Received request to get order by ID: {}", id);
        OrderResponseDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @Operation(summary = "Atualizar pedido", description = "Atualiza os dados de um pedido existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> updateOrder(@PathVariable UUID id, @RequestBody @Valid OrderRequestDTO request) {
        log.info("Received request to update order ID: {}", id);
        OrderResponseDTO updated = orderService.updateOrder(id, request);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Deletar pedido", description = "Remove um pedido pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pedido deletado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID id) {
        log.info("Received request to delete order ID: {}", id);
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
