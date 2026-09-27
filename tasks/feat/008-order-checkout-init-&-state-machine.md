# 008 - Order Checkout Init & State Machine

- Tipo: feat
- Status: Concluído
- Depende de: tasks/feat/007-ticket-type-service-and-inventory.md

## Contexto
Implementar a criação síncrona da ordem (`OrderService.createOrder`) vinculando usuário, endereço de cobrança e tipo de ingresso, calculando valor total e atribuindo o status inicial `PENDING`.

## Escopo
- Mapeamento e validação de `OrderRequestDTO`
- Criação e persistência da entidade `Order` com status `OrderStatus.PENDING`
- Cálculo de `totalAmount` baseado no `quantity` solicitado e `price` do `TicketType`
- Testes unitários com Mockito cobrindo cálculo e validações de estoque estático

## Fora de escopo
- Trava distribuída ou decremento com Redis (será a task 009)
- Envio para filas RabbitMQ
- Gateway real ou simulação de pagamento

## Requisitos
- Se `quantity` for maior que `quantityAvailable` no banco, lançar `BusinessRuleException("Estoque insuficiente")`
- A ordem deve vincular obrigatoriamente um `BillingAddress` válido pertencente ao usuário autenticado

## Critérios de aceite
- [x] Criação de Order com cálculo correto de total amount
- [x] Bloqueio com exception se o estoque relacional for insuficiente
- [x] Testes unitários passando via `mvn test -Dtest=OrderServiceTest`

## Arquivos prováveis
- src/main/java/com/hanniel/ticketBookingSystem/services/order/OrderService.java
- src/main/java/com/hanniel/ticketBookingSystem/dtos/order/OrderRequestDTO.java
- src/main/java/com/hanniel/ticketBookingSystem/dtos/order/OrderResponseDTO.java
- src/test/java/com/hanniel/ticketBookingSystem/services/order/OrderServiceTest.java

## Questões em aberto
- Nenhuma