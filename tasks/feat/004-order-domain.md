# Feature: Gestão de Pedidos (Order)

## Status
- **Status:** Concluído
- **Commits Relacionados:** `491858d`

---

## Escopo e Objetivos
Criação e rastreamento de pedidos de compra de ingressos (`Order`), relacionando usuário comprador, tipo de ingresso, endereço fiscal e status de processamento da transação.

---

## Estrutura de Arquivos Criados

### 1. Domínio (`domain/order`)
- `Order.java`: Entidade contendo `id`, `userId`, `ticketTypeId`, `quantity`, `totalAmount`, `status`, `billingAddressId` e `createdAt`.
- `OrderStatus.java`: Enum com estados do pedido (`PENDING`, `COMPLETED`, `CANCELLED`).

### 2. DTOs e Mappers
- `OrderRequestDTO.java`: Record de entrada para realização de pedidos com campos de validação.
- `OrderResponseDTO.java`: Record de resposta detalhada com informações do pedido e total.
- `OrderMapper.java`: Mapper via MapStruct entre entidade e Records.

### 3. Repositório e Serviço
- `OrderRepository.java`: Interface Spring Data JPA para persistência dos pedidos.
- `OrderService.java`: Lógica de processamento de pedidos, validação de dados do comprador, cálculo de valor e atribuição de status inicial.

### 4. Controller (`controllers/order`)
- `OrderController.java`: Endpoints REST expostos sob `/orders`:
  - `POST /orders`: Criação de novo pedido.
  - `GET /orders`: Listagem geral.
  - `GET /orders/{id}`: Detalhes do pedido por UUID.
  - `PUT /orders/{id}`: Atualização de pedido.
  - `DELETE /orders/{id}`: Cancelamento/remoção de pedido.

### 5. Testes Unitários
- `OrderControllerTest.java`
- `OrderServiceTest.java`
