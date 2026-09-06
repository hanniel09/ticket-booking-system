# Feature: Gestão de Eventos

## Status
- **Status:** Concluído
- **Commits Relacionados:** `242a3b4`, `500d089`, `a6a65e6`

---

## Escopo e Objetivos
Gerenciamento do ciclo de vida de eventos, incluindo cadastro, consulta, atualização e exclusão, além de suporte a formato de data customizado brasileiro (`DD/MM/YYYY`).

---

## Estrutura de Arquivos Criados

### 1. Domínio (`domain/event`)
- `Event.java`: Entidade com `id` (UUID), `name` (String) e `eventDate` (`ZonedDateTime`).

### 2. DTOs e Mappers
- `EventRequestDTO.java`: Record para criação/atualização de eventos contendo nome e data no formato `DD/MM/YYYY`.
- `EventResponseDTO.java`: Record com os dados do evento retornados ao cliente.
- `EventMapper.java`: Interface MapStruct para conversão bidirecional entre `Event`, `EventRequestDTO` e `EventResponseDTO` utilizando `DateHelper`.

### 3. Repositório e Serviço
- `EventRepository.java`: Interface Spring Data JPA para operações com a tabela `event`.
- `EventService.java`: Camada de regras de negócio com métodos para criação, busca por ID, listagem, atualização e deleção com lançamento de `ResourceNotFoundException`.

### 4. Controller (`controllers/event`)
- `EventController.java`: Endpoints REST mapeados sob `/events`:
  - `POST /events`: Criação de novo evento.
  - `GET /events`: Listagem de todos os eventos.
  - `GET /events/{id}`: Busca de evento específico por UUID.
  - `PUT /events/{id}`: Atualização de evento existente.
  - `DELETE /events/{id}`: Exclusão de evento por UUID.

### 5. Testes Unitários
- `EventControllerTest.java`: Testes dos endpoints HTTP com MockMvc.
- `EventServiceTest.java`: Testes unitários com Mockito.
