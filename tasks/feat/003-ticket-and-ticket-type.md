# Feature: Ingressos e Tipos de Ingresso (Ticket & TicketType)

## Status
- **Status:** Concluído
- **Commits Relacionados:** `2b4bd93`, `5b311da`

---

## Escopo e Objetivos
Definição das categorias de ingresso vinculadas a eventos (`TicketType`) e controle individual dos ingressos emitidos (`Ticket`) com gerenciamento de seus estados.

---

## Estrutura de Arquivos Criados

### 1. Tipos de Ingresso (`TicketType`)
- **Domínio:** `TicketType.java` com `id`, `eventId`, `name`, `price` (`BigDecimal`) e `quantityAvailable` (`Long`).
- **DTOs:** `TicketTypeRequestDTO.java`, `TicketTypeResponseDTO.java`.
- **Mapper:** `TicketTypeMapper.java` via MapStruct.
- **Repositório:** `TicketTypeRepository.java`.
- **Serviço:** `TicketTypeService.java` com operações CRUD e validação de existência de evento.
- **Controller:** `TicketTypeController.java` sob `/ticket-types`.
- **Testes:** `TicketTypeControllerTest.java`, `TicketTypeServiceTest.java`.

### 2. Ingressos Individuais (`Ticket`)
- **Domínio:** `Ticket.java` com `id`, `ticketCode` único, `eventId`, `pricePaid`, `ticketTypeId`, `status` (`TicketStatus`), `purchaseDate` e `orderId`.
- **Enum de Status:** `TicketStatus.java` (`AVAILABLE`, `RESERVED`, `PAID`, `CANCELLED`).
- **DTOs:** `TicketRequestDTO.java`, `TicketResponseDTO.java`.
- **Mapper:** `TicketMapper.java` via MapStruct.
- **Repositório:** `TicketRepository.java` (inclui buscas por código, status e evento).
- **Serviço:** `TicketService.java` com emissão, validação de status e operações de ciclo de vida do ingresso.
- **Controller:** `TicketController.java` sob `/tickets`.
- **Testes:** `TicketControllerTest.java`, `TicketServiceTest.java`.
