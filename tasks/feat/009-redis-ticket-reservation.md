# 009 - Temporary Ticket Reservation with Redis

- Tipo: feat
- Status: todo
- Depende de: tasks/feat/008-order-checkout-init.md

## Contexto
Implementar a reserva atômica temporária de ingressos em memória via Redis com TTL de 10 minutos para prevenir overselling e evitar lock de linha no PostgreSQL durante o fluxo de checkout.

## Escopo
- Criação de componente ou serviço de reserva em memória (`TicketInventoryRedisService`)
- Operação de decremento atômico de estoque utilizando Redis (`opsForValue().decrement`)
- Definição de chave padronizada: `inventory:ticket-type:{ticketTypeId}`
- Rollback/Incremento atômico no Redis caso a criação da ordem no Postgres falhe
- Configuração de expiração (TTL de 10 minutos) na chave ou registro de reserva
- Testes unitários com Mockito cobrindo cenários de sucesso, estoque esgotado e reversão de contagem

## Fora de escopo
- Disparo de eventos ou integração com RabbitMQ (task 010)
- Simulação de pagamento e confirmação definitiva do pedido
- Rotinas agendadas (Cron jobs) para expiração no banco relacional

## Requisitos
- Se o decremento atômico resultar em valor menor que 0, a operação deve reverter o decremento (`increment`) e lançar `BusinessRuleException("Ingressos esgotados no lote")`
- A chamada ao Redis deve ser idempotente e não pode gerar estado inconsistente entre o cache e o banco relacional
- Não utilizar `@Transactional` diretamente sobre métodos puramente de Redis

## Critérios de aceite
- [ ] Decremento atômico no Redis validado antes de persistir a Order no PostgreSQL
- [ ] Rollback no Redis disparado se a transação do Postgres falhar
- [ ] Bloqueio de checkout com exception customizada quando o saldo no Redis for zero
- [ ] Testes unitários cobrindo sucesso e concorrência teórica passando via `./mvnw test -Dtest=TicketInventoryRedisServiceTest`

## Arquivos prováveis
- src/main/java/com/hanniel/ticketBookingSystem/services/ticket/TicketInventoryRedisService.java
- src/main/java/com/hanniel/ticketBookingSystem/services/order/OrderService.java
- src/test/java/com/hanniel/ticketBookingSystem/services/ticket/TicketInventoryRedisServiceTest.java

## Questões em aberto
- Nenhuma