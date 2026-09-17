# Revisão Técnica do Projeto: Ticket Booking System

Documento central de controle, rastreabilidade e planejamento evolutivo do projeto.

---

## 📌 Visão Geral do Progresso

O projeto encontra-se com sua infraestrutura base, camadas de domínio principais, autenticação stateless JWT, persistência relacional com Flyway, documentação interativa via OpenAPI/Swagger UI e suíte de testes (camadas de serviço e web MVC) plenamente estabelecidas, seguindo com rigor as diretrizes definidas em [architecture.md](file:///D:/dev/ticket-booking-system/architecture.md) e [agents.md](file:///D:/dev/ticket-booking-system/agents.md).

O histórico de desenvolvimento está estruturado nas categorias **Setup** e **Features** conforme detalhado a seguir.

---

## 🏗️ 1. Tarefas de Configuração e Infraestrutura (`tasks/setup`)

| Arquivo de Detalhamento | Escopo | Status |
| :--- | :--- | :--- |
| [001-initial-setup-and-build.md](file:///D:/dev/ticket-booking-system/tasks/setup/001-initial-setup-and-build.md) | Configuração do Java 25, Maven, starters Spring Boot e integração Lombok/MapStruct | Concluído |
| [002-docker-and-compose.md](file:///D:/dev/ticket-booking-system/tasks/setup/002-docker-and-compose.md) | Multi-stage Dockerfile e orquestração dos serviços (PostgreSQL 17, Redis 7.2, App) | Concluído |
| [003-database-and-migrations.md](file:///D:/dev/ticket-booking-system/tasks/setup/003-database-and-migrations.md) | Conexão PostgreSQL e scripts de migração estrutural com Flyway (`V1__init_schema.sql`) | Concluído |
| [004-redis-cache.md](file:///D:/dev/ticket-booking-system/tasks/setup/004-redis-cache.md) | Configuração do RedisTemplate e pool de conexões Lettuce | Concluído |
| [005-add-springdoc-openapi.md](file:///D:/dev/ticket-booking-system/tasks/setup/005-add-springdoc-openapi.md) | Integração do SpringDoc OpenAPI UI 2.8.5, Swagger UI, Bearer JWT e anotações completas de documentação | Concluído |

---

## 🚀 2. Tarefas de Funcionalidades e Domínio (`tasks/feat`)

| Arquivo de Detalhamento | Escopo | Endpoints Principais | Status |
| :--- | :--- | :--- | :--- |
| [001-auth-and-security.md](file:///D:/dev/ticket-booking-system/tasks/feat/001-auth-and-security.md) | Autenticação stateless com JWT, filtro de segurança, criptografia BCrypt e papéis de acesso | `/auth/login`, `/auth/register` | Concluído |
| [002-event-domain.md](file:///D:/dev/ticket-booking-system/tasks/feat/002-event-domain.md) | CRUD completo de Eventos com tratamento de datas e exceções de recursos | `/events/**` | Concluído |
| [003-ticket-and-ticket-type.md](file:///D:/dev/ticket-booking-system/tasks/feat/003-ticket-and-ticket-type.md) | Gerenciamento de tipos de ingresso (`TicketType`) e ciclo de vida dos ingressos emitidos (`Ticket`) | `/tickets/types/**`, `/tickets/**` | Concluído |
| [004-order-domain.md](file:///D:/dev/ticket-booking-system/tasks/feat/004-order-domain.md) | Criação, consulta e atualização de transações de pedidos vinculadas a usuários | `/orders/**` | Concluído |
| [005-billing-address.md](file:///D:/dev/ticket-booking-system/tasks/feat/005-billing-address.md) | Gestão de dados fiscais obrigatórios para checkout (CPF/CNPJ, CEP, endereço) | `/billing-addresses/**` | Concluído |
| [006-date-helper.md](file:///D:/dev/ticket-booking-system/tasks/feat/006-date-helper.md) | Utilitário central de parsing e formatação no padrão `DD/MM/YYYY` | Integrado aos mappers | Concluído |
| [006-event-service-and-validation.md](file:///D:/dev/ticket-booking-system/tasks/feat/006-event-service-and-validation.md) | Validação temporal de eventos (bloqueio retroativo), método `findById` e testes de contrato | `/events/**` | Concluído |

---

## 🔍 3. Conformidade Arquitetural

| Diretriz | Implementação no Projeto | Situação |
| :--- | :--- | :--- |
| **Java 25 & Records** | Records utilizados em todos os DTOs de request e response | Totalmente aderente |
| **Arquitetura em Camadas** | Separação estrita Controller -> Service -> Repository -> Entity | Totalmente aderente |
| **Sub-pacotes por Domínio** | Isolamento estrito por feature (`auth`, `event`, `order`, `ticket`, `billingAddress`) | Totalmente aderente |
| **Imutabilidade e DTOs** | Entidades JPA não expostas diretamente nos Controllers; uso de MapStruct | Totalmente aderente |
| **Tratamento de Exceções** | `GlobalExceptionHandler` unificado com formato de erro padronizado | Totalmente aderente |
| **Testes Unitários e Integração Web** | Testes para cada Service (Mockito) e Controller (MockMvc sem filtros restritivos) | Totalmente aderente |
| **OpenAPI / Swagger Documentation** | SpringDoc OpenAPI com `@Tag`, `@Operation`, `@ApiResponse`, `@Schema` nos Records e esquema Bearer JWT | Totalmente aderente |

---

## 🎯 4. Próximos Passos e Backlog Arquitetural

Para atingir a totalidade dos requisitos de alta concorrência e resiliência descritos em [architecture.md](file:///D:/dev/ticket-booking-system/architecture.md):

1. **Controle Atômico de Estoque no Redis (Reserva Temporária & Lock Distribuído):**
   - Implementar decrepitação atômica com `DECRBY` no Redis ao selecionar ingressos.
   - Estabelecer TTL de 10 minutos para retenção da reserva e bloqueio de concorrência distribuída.
   - Prevenir rigorosamente o overselling sob picos de tráfego.

2. **Topologia Orientada a Eventos (RabbitMQ):**
   - Adicionar dependência `spring-boot-starter-amqp` ao `pom.xml`.
   - Criar fila de pagamento imediato (`ticket.payment.queue`) para processamento assíncrono de pagamentos.
   - Configurar fila com atraso (`ticket.delayed.exchange`) de 10 minutos como árbitro oficial de expiração de pedidos.
   - Implementar worker consumidor para processamento de transações e reversão via `INCRBY` no Redis caso o pedido não seja pago no prazo.

3. **Notificações em Tempo Real e Assíncronas:**
   - Implementar Server-Sent Events (SSE) para atualização instantânea do status do pedido e falhas de pagamento no frontend.
   - Disparo de email transacional assíncrono após confirmação de pagamento.

4. **Fila de Espera Virtual (Pre-Sale Waiting Room):**
   - Sistema de fila virtual de espera com admissão controlada 30 minutos antes do início oficial das vendas para proteger os recursos de banco de dados.
