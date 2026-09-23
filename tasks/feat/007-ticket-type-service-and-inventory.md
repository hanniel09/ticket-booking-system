# Task Spec: 007 - TicketType Service, Stock Definition & Contract Tests

## Status
- **Status:** Concluído

---

## 1. Context & Objective
Implementar as regras de negócio para criação e consulta de tipos/lotes de ingressos (`TicketTypeService`) associados a um evento existente. Validar consistência de precificação (não negativa), estoque inicial (positivo) e garantir integridade referencial com a entidade `Event`.

## 2. Target Scope Boundaries

### Writable Files
- `src/main/java/com/hanniel/ticketBookingSystem/services/ticket/TicketTypeService.java`
- `src/main/java/com/hanniel/ticketBookingSystem/dtos/ticket/TicketTypeRequestDTO.java`
- `src/main/java/com/hanniel/ticketBookingSystem/dtos/ticket/TicketTypeResponseDTO.java`
- `src/test/java/com/hanniel/ticketBookingSystem/services/ticket/TicketTypeServiceTest.java`

### Read-Only Context
- `src/main/java/com/hanniel/ticketBookingSystem/domain/ticket/TicketType.java`
- `src/main/java/com/hanniel/ticketBookingSystem/repositories/ticket/TicketTypeRepository.java`
- `src/main/java/com/hanniel/ticketBookingSystem/repositories/event/EventRepository.java`
- `src/main/java/com/hanniel/ticketBookingSystem/mappers/ticket/TicketTypeMapper.java`

### Forbidden Zone
- NÃO alterar contratos ou regras de `Order`, `Ticket` ou `BillingAddress`.
- NÃO implementar ainda a trava de concorrência com Redis (será abordada no OrderService).

---

## 3. Strict Data Contracts

### TicketTypeRequestDTO (Record)
- `eventId`: `@NotNull(message = "O ID do evento é obrigatório")`
- `name`: `@NotBlank(message = "O nome da categoria do ingresso é obrigatório")`
- `price`: `@NotNull`, `@DecimalMin(value = "0.0", inclusive = true, message = "O preço não pode ser negativo")`
- `quantityAvailable`: `@NotNull`, `@Min(value = 1, message = "A quantidade inicial deve ser de no mínimo 1 ingresso")`

### Regras de Negócio (TicketTypeService)
- `createTicketType(TicketTypeRequestDTO dto)`:
    - Validar se o `eventId` existe no `EventRepository`. Caso não exista, lançar `ResourceNotFoundException("Evento não encontrado")`.
    - Salvar o `TicketType` vinculado ao ID do evento com `@Transactional`.
- `findByEventId(UUID eventId)`: Retornar lista de `TicketTypeResponseDTO` disponíveis para o evento.
- `findById(UUID id)`: Retornar `TicketTypeResponseDTO` ou lançar `ResourceNotFoundException`.

---

## 4. Implementation Checklist (Live State)

- [x] **Passo 1:** Configurar anotações de validação (`@NotNull`, `@Min`, `@DecimalMin`, `@NotBlank`) no record `TicketTypeRequestDTO`.
- [x] **Passo 2:** Implementar métodos em `TicketTypeService` com injeção de `TicketTypeRepository`, `EventRepository` e `TicketTypeMapper`.
- [x] **Passo 3:** Criar testes unitários em `TicketTypeServiceTest` com Mockito cobrindo:
    - Criação bem-sucedida de `TicketType` com evento existente.
    - Tentativa de criação com `eventId` inexistente (deve lançar `ResourceNotFoundException`).
    - Consulta por ID existente e inexistente.

---

## 5. Verification Gate (Run before marking as DONE)

```bash
# 1. Validação de compilação
./mvnw test-compile

# 2. Execução focada nos testes de TicketType
./mvnw test -Dtest=TicketTypeServiceTest