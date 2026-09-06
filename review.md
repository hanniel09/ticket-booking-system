# Revisão Técnica do Projeto: Ticket Booking System

Documento central de controle, rastreabilidade e planejamento evolutivo do projeto.

---

## 📌 Visão Geral do Progresso

O projeto encontra-se com sua infraestrutura base, camadas de domínio principais, autenticação e persistência plenamente estabelecidas, seguindo as diretrizes arquiteturais definidas em [architecture.md](file:///D:/dev/ticket-booking-system/architecture.md) e [agents.md](file:///D:/dev/ticket-booking-system/agents.md).

O histórico de desenvolvimento está organizado nas categorias **Setup** e **Features** conforme detalhado a seguir.

---

## 🏗️ 1. Tarefas de Configuração e Infraestrutura (`tasks/setup`)

| Arquivo de Detalhamento | Escopo | Status |
| :--- | :--- | :--- |
| [001-initial-setup-and-build.md](file:///D:/dev/ticket-booking-system/tasks/setup/001-initial-setup-and-build.md) | Configuração do Java 25, Maven, starters Spring Boot e integração Lombok/MapStruct | Concluído |
| [002-docker-and-compose.md](file:///D:/dev/ticket-booking-system/tasks/setup/002-docker-and-compose.md) | Multi-stage Dockerfile e orquestração dos serviços (PostgreSQL 17, Redis 7.2, App) | Concluído |
| [003-database-and-migrations.md](file:///D:/dev/ticket-booking-system/tasks/setup/003-database-and-migrations.md) | Conexão PostgreSQL e scripts de migração estrutural com Flyway (`V1__init_schema.sql`) | Concluído |
| [004-redis-cache.md](file:///D:/dev/ticket-booking-system/tasks/setup/004-redis-cache.md) | Configuração do RedisTemplate e pool de conexões Lettuce | Concluído |

---

## 🚀 2. Tarefas de Funcionalidades e Domínio (`tasks/feat`)

| Arquivo de Detalhamento | Escopo | Endpoints Principais | Status |
| :--- | :--- | :--- | :--- |
| [001-auth-and-security.md](file:///D:/dev/ticket-booking-system/tasks/feat/001-auth-and-security.md) | Autenticação stateless com JWT, filtro de segurança, criptografia BCrypt e papéis de acesso | `/auth/login`, `/auth/register` | Concluído |
| [002-event-domain.md](file:///D:/dev/ticket-booking-system/tasks/feat/002-event-domain.md) | CRUD completo de Eventos com tratamento de datas e exceções de recursos | `/events/**` | Concluído |
| [003-ticket-and-ticket-type.md](file:///D:/dev/ticket-booking-system/tasks/feat/003-ticket-and-ticket-type.md) | Gerenciamento de tipos de ingresso (`TicketType`) e ciclo de vida dos ingressos emitidos (`Ticket`) | `/ticket-types/**`, `/tickets/**` | Concluído |
| [004-order-domain.md](file:///D:/dev/ticket-booking-system/tasks/feat/004-order-domain.md) | Criação, consulta e atualização de transações de pedidos vinculadas a usuários | `/orders/**` | Concluído |
| [005-billing-address.md](file:///D:/dev/ticket-booking-system/tasks/feat/005-billing-address.md) | Gestão de dados fiscais obrigatórios para checkout (CPF/CNPJ, CEP, endereço) | `/billing-address/**` | Concluído |
| [006-date-helper.md](file:///D:/dev/ticket-booking-system/tasks/feat/006-date-helper.md) | Utilitário central de parsing e formatação no padrão `DD/MM/YYYY` | Integrado aos mappers | Concluído |

---

## 🔍 3. Conformidade Arquitetural

| Diretriz | Implementação no Projeto | Situação |
| :--- | :--- | :--- |
| **Java 25 & Records** | Records utilizados em todos os DTOs de request e response | Totalmente aderente |
| **Arquitetura em Camadas** | Separação estrita Controller -> Service -> Repository -> Entity | Totalmente aderente |
| **Sub-pacotes por Domínio** | Isolamento por feature (`auth`, `event`, `order`, `ticket`, `billingAddress`) | Totalmente aderente |
| **Imutabilidade e DTOs** | Entidades JPA não expostas diretamente nos Controllers; uso de MapStruct | Totalmente aderente |
| **Tratamento de Exceções** | `GlobalExceptionHandler` unificado com formato de erro padronizado | Totalmente aderente |
| **Testes Unitários** | Testes para cada Service (Mockito) e Controller (MockMvc) | Totalmente aderente |

---

## 🎯 4. Próximos Passos e Backlog Arquitetural

Para atingir todos os requisitos de alta concorrência descritos em [architecture.md](file:///D:/dev/ticket-booking-system/architecture.md):

1. **Controle Atômico de Estoque no Redis:**
   - Implementar reserva temporária com `DECRBY` no Redis e bloqueio distribuído com TTL de 10 minutos.
2. **Topologia Orientada a Eventos (RabbitMQ):**
   - Configuração de mensageria com fila imediata (`ticket.payment.queue`) e fila com atraso (`ticket.delayed.exchange`) de 10 minutos para reversão de estoque em caso de não pagamento.
3. **Mecanismo de Fila de Espera Virtual:**
   - Gerenciamento de tráfego 30 minutos antes da abertura de vendas para evitar saturação do banco de dados.
4. **Notificações em Tempo Real:**
   - Atualização de status para o frontend via Server-Sent Events (SSE) e envio assíncrono de confirmação por email.
