# Setup: Docker e Docker Compose

## Status
- **Status:** Concluído
- **Commits Relacionados:** `b64ddab`, `c3c2d8f`, `2d1c46b`

---

## Escopo e Objetivos
Orquestração e conteinerização de todos os serviços da aplicação (PostgreSQL, Redis e API Spring Boot) para garantir paridade entre ambientes de desenvolvimento e produção.

---

## Componentes Configurados

### 1. Dockerfile Multi-Stage (`Dockerfile`)
Construção otimizada em duas etapas:
- **Build Stage:** Utiliza a imagem `maven:3.9-eclipse-temurin-25` para compilar e empacotar a aplicação via `mvn clean package -DskipTests`.
- **Runtime Stage:** Imagem enxuta `eclipse-temurin:25-jre`, expondo a porta `8080` e executando o `.jar` gerado.

### 2. Orquestração (`docker-compose.yml`)
Configuração de 3 serviços conectados pela rede interna `ticket-booking-service`:

1. **`db` (PostgreSQL 17):**
   - Container: `ticker-booking-postgres`
   - Porta mapeada: `5430:5432` (evita conflitos na porta padrão 5432 local)
   - Volume: `postgres_data` para persistência de dados
   - Credenciais: banco `ticket-booking`, usuário `postgres`, senha `123456`

2. **`redis` (Redis 7.2-alpine):**
   - Container: `ticket-redis`
   - Porta: `6379:6379`
   - Volume: `redis_data`
   - Autenticação e persistência: `redis-server --appendonly yes --requirepass supersecretpassword`

3. **`app` (Spring Boot API):**
   - Container: `ticket-booking-app`
   - Porta: `8080:8080`
   - Dependências: aguarda `db` e `redis`
   - Variáveis de ambiente integradas com as instâncias conteinerizadas.

---

## Como Executar
```bash
# Subir toda a infraestrutura e a aplicação
docker compose up --build -d

# Subir apenas bancos (dev local)
docker compose up -d db redis
```
