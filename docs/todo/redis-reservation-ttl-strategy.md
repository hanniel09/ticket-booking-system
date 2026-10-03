# Estratégia de Reserva Temporária de Ingressos com TTL no Redis

## 1. O Problema Identificado
Atualmente, o `DEFAULT_TTL` de 10 minutos está aplicado diretamente na chave mestre de estoque:
- `inventory:ticket-type:{ticketTypeId}`

**Impacto:** O lote expira e é removido da memória do Redis 10 minutos após ser cadastrado, impossibilitando qualquer compra posterior.

---

## 2. Separação Arquitetural das Chaves no Redis

Para suportar o tempo de retenção do checkout (holding de pagamento) sem invalidar o estoque mestre, deve-se adotar duas chaves com responsabilidades distintas:

### Chave 1: Estoque Mestre (Disponibilidade Real)
- **Padrão:** `inventory:ticket-type:{ticketTypeId}`
- **Tipo:** Inteiro / Long (`opsForValue`)
- **TTL:** Nenhum (ou data de término do evento)
- **Função:** Saldo em tempo real do lote. Decrementado via `DECRBY` e incrementado via `INCRBY`.

### Chave 2: Reserva Temporária de Pedido (Checkout Hold)
- **Padrão:** `reservation:order:{orderId}`
- **Valor:** Metadados da reserva (`ticketTypeId`, `quantity`, `userId`)
- **TTL:** 10 minutos (`Duration.ofMinutes(10)`)
- **Função:** Garante a janela de 10 minutos para pagamento.

---

## 3. Fluxo de Vida da Reserva e Pagamento

```
[Checkout Iniciado]
        │
        ▼
Decrementa Chave Mestre (inventory:ticket-type:{id})
        │
        ├──> Saldo insuficiente? ──> Lança BusinessRuleException
        │
        ▼
Cria Chave de Reserva (reservation:order:{orderId}) com TTL de 10 min
        │
        ▼
Persiste Order no PostgreSQL (Status: PENDING)
        │
        ├──> Falha no Postgres? ──> Rollback manual imediato (releaseStock)
        │
        ▼
[Aguardando Pagamento]
        │
        ├── A. Pagamento Aprovado:
        │      - Remove reservation:order:{orderId}
        │      - Atualiza Order para CONFIRMED
        │
        └── B. Tempo Expirado (10 min sem pagamento) ou Cancelamento:
               - Incrementa Chave Mestre (releaseStock)
               - Atualiza Order para CANCELED / EXPIRED
```

---

## 4. Estratégias para Reversão do Estoque Expirado

A devolução do estoque após os 10 minutos pode ser resolvida por três abordagens:

1. **Mensageria com RabbitMQ (Task 010 - Recomendado):**
   - Ao criar a ordem, publica mensagem em fila com TTL/Delay de 10 minutos.
   - O consumidor checa se a ordem continua `PENDING`; caso positivo, cancela a ordem e chama `releaseStock`.

2. **Redis Keyspace Notifications (`KeyExpirationEventMessageListener`):**
   - Configurar o Redis para disparar evento na expiração de `reservation:order:{orderId}`.
   - Um listener no Spring intercepta o evento e devolve a quantidade para o lote.

3. **Job Agendado (`@Scheduled` / Cron):**
   - Polling a cada 1 minuto buscando pedidos `PENDING` com `createdAt < now() - 10 min`.

---

## 5. Itens a Executar (Checklist para o Próximo Dia)

- [ ] **`TicketInventoryRedisService`:**
  - Remover `DEFAULT_TTL` do método `initStock`.
  - Criar método `createReservationHold(UUID orderId, UUID ticketTypeId, Long quantity, Duration ttl)`.
  - Criar método `deleteReservationHold(UUID orderId)`.
  - Criar método `deleteStock(UUID ticketTypeId)` para uso na exclusão de lotes.
- [ ] **`TicketTypeService`:**
  - Garantir que `initStock` seja chamado sem TTL no lote.
  - Sincronizar exclusão de lote (`deleteTicketType`) com `deleteStock`.
  - Sincronizar atualização de lote (`updateTicketType`) com redefinição de estoque.
- [ ] **`OrderService`:**
  - Registrar a reserva temporária (`createReservationHold`) vinculada ao pedido criado.
- [ ] **Testes:**
  - Atualizar e validar a suíte de testes com Mockito cobrindo a nova separação de chaves.
