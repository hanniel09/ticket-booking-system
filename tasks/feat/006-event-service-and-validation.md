# Task Spec: 006 - Event Service, Validation & Contract Tests

## Status
- **Status:** Concluído

---

## 1. Context & Objective
Implementar a regra de negócio para criação e consulta de Eventos (`EventService`), garantindo validações de integridade temporal (data do evento não pode ser no passado) e integrando o `DateHelper` ao ciclo de vida do DTO/Entity.

---

## 2. Target Scope Boundaries

### Writable Files
- `src/main/java/com/hanniel/ticketBookingSystem/services/event/EventService.java`
- `src/main/java/com/hanniel/ticketBookingSystem/dtos/event/EventRequestDTO.java`
- `src/main/java/com/hanniel/ticketBookingSystem/dtos/event/EventResponseDTO.java`
- `src/test/java/com/hanniel/ticketBookingSystem/services/event/EventServiceTest.java`

### Read-Only Context
- `src/main/java/com/hanniel/ticketBookingSystem/helper/date/DateHelper.java`
- `src/main/java/com/hanniel/ticketBookingSystem/mappers/event/EventMapper.java`
- `src/main/java/com/hanniel/ticketBookingSystem/domain/event/Event.java`
- `src/main/java/com/hanniel/ticketBookingSystem/repositories/event/EventRepository.java`

### Forbidden Zone
- NÃO alterar controllers ou contratos de `Order`, `Ticket` ou `BillingAddress`.
- NÃO alterar migrações do Flyway.

---

## 3. Strict Data Contracts

### EventRequestDTO (Record)
- `name`: `@NotBlank(message = "O nome do evento é obrigatório")`
- `eventDate`: `@NotBlank(message = "A data do evento é obrigatória")`, aceita formato `"dd/MM/yyyy"` e é validada via `DateHelper`.

### Regras de Negócio (EventService)
- `createEvent(EventRequestDTO dto)`: Converte a data usando `DateHelper`. Se a data calculada for anterior ao momento atual (`ZonedDateTime.now()`), lança `IllegalArgumentException` com a mensagem `"A data do evento não pode ser retroativa"`.
- `findById(UUID id)`: Retorna `EventResponseDTO` ou lança `ResourceNotFoundException`.

---

## 4. Implementation Checklist (Live State)

- [x] **Passo 1:** Adicionar anotações de validação de campo (`@NotBlank`) em `EventRequestDTO`.
- [x] **Passo 2:** Implementar métodos `createEvent` e `findById` em `EventService` com `@Transactional`.
- [x] **Passo 3:** Criar `EventServiceTest` cobrindo:
    - Criação de evento com data válida (sucesso).
    - Bloqueio de evento com data no passado (deve lançar exceção).
    - Busca de evento por ID inexistente (deve lançar `ResourceNotFoundException`).

---

## 5. Verification Gate (Run before marking as DONE)

```bash
# 1. Validação de compilação
./mvnw test-compile

# 2. Execução focada nos testes de Event
./mvnw test -Dtest=EventServiceTest,DateHelperTest
```