# Setup: Banco de Dados e Migrações Flyway

## Status
- **Status:** Concluído
- **Commits Relacionados:** `a5e1b85`, `2d1c46b`

---

## Escopo e Objetivos
Configuração do banco de dados relacional PostgreSQL, suporte a UUIDs, e versionamento de esquema através do Flyway Migration.

---

## Componentes Configurados

### 1. Dependências e Driver
- `org.postgresql:postgresql` (runtime)
- `org.flywaydb:flyway-database-postgresql` (11.11.2, runtime)

### 2. Configurações (`application.properties`)
- `spring.datasource.url=jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5430}/ticket-booking`
- `spring.datasource.username=${DB_USER:postgres}`
- `spring.datasource.password=${DB_PASSWORD:123456}`
- `spring.jpa.hibernate.ddl-auto=update`

### 3. Script de Migração Inicial (`V1__init_schema.sql`)
Localização: `src/main/resources/db/migration/V1__init_schema.sql`

Tabelas criadas com chaves primárias do tipo `UUID` e chaves estrangeiras:
- `users`: Armazena credenciais e papéis de acesso (`role`).
- `billing_address`: Armazena endereços fiscais vinculados ao usuário (`user_id`).
- `event`: Armazena informações dos eventos e datas com timezone.
- `ticket_type`: Define categorias de ingressos vinculadas ao evento (`event_id`), preço e estoque.
- `orders`: Registra transações de pedidos vinculadas a usuário, tipo de ingresso e endereço de cobrança.
- `ticket`: Registra unidades individuais de ingressos emitidos com código único (`ticket_code`), status e vínculo ao pedido (`order_id`).
