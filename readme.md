# Ticket Booking System

Sistema de reserva e venda de ingressos para eventos de alta concorrência desenvolvido com **Spring Boot 4 / 3.x** e **Java 25**.

---

## 📋 Pré-requisitos

Antes de iniciar, certifique-se de possuir instalado em sua máquina:

- [Java 25](https://adoptium.net/) (JDK 25)
- [Docker](https://www.docker.com/) e **Docker Compose**
- [Maven 3.9+](https://maven.apache.org/) (opcional, já incluído via wrapper `mvnw`)

---

## 🚀 Como Rodar o Projeto

Você pode executar o projeto de duas formas:
1. **Totalmente via Docker Compose** (recomendado para testar o ambiente completo).
2. **Ambiente de Desenvolvimento Local** (infraestrutura no Docker e aplicação rodando localmente).

---

### Opção 1: Execução Completa via Docker Compose

Esta opção constrói a imagem da aplicação e inicia o banco PostgreSQL, o cache Redis e a API Spring Boot em containers integrados.

1. Suba todos os serviços:
   ```bash
   docker compose up --build
   ```

   *(Opcional) Para rodar em segundo plano (detached mode):*
   ```bash
   docker compose up --build -d
   ```

2. Para encerrar os serviços:
   ```bash
   docker compose down
   ```

---

### Opção 2: Desenvolvimento Local (Infra no Docker + App Local)

Neste fluxo, você sobe apenas o banco de dados e o Redis via Docker e executa a aplicação Spring Boot diretamente na sua máquina.

#### 1. Inicie a infraestrutura (PostgreSQL e Redis)

```bash
docker compose up -d db redis
```

#### 2. Execute a aplicação Spring Boot

- **Linux / macOS:**
  ```bash
  ./mvnw spring-boot:run
  ```

- **Windows (PowerShell / CMD):**
  ```powershell
  .\mvnw.cmd spring-boot:run
  ```

A aplicação será inicializada e as tabelas serão criadas/migradas automaticamente pelo **Flyway**.

---

## ⚙️ Configurações e Variáveis de Ambiente

As configurações principais estão centralizadas em `src/main/resources/application.properties` e podem ser sobrescritas por variáveis de ambiente:

| Variável | Descrição | Valor Padrão (Local) | Valor no Docker Compose |
| :--- | :--- | :--- | :--- |
| `DB_HOST` | Endereço do PostgreSQL | `localhost` | `db` |
| `DB_PORT` | Porta de conexão do PostgreSQL | `5430` | `5432` |
| `DB_USER` | Usuário do PostgreSQL | `postgres` | `postgres` |
| `DB_PASSWORD` | Senha do PostgreSQL | `123456` | `123456` |
| `SPRING_DATA_REDIS_HOST` | Endereço do Redis | `localhost` | `redis` |
| `SPRING_DATA_REDIS_PORT` | Porta do Redis | `6379` | `6379` |
| `SPRING_DATA_REDIS_PASSWORD` | Senha do Redis | `supersecretpassword` | `supersecretpassword` |
| `token.secret` | Chave secreta de assinatura JWT | `supersecret` | `supersecret` |

---

## 🌐 Portas e Endereços dos Serviços

- **API Spring Boot**: `http://localhost:8080`
- **PostgreSQL**: `localhost:5430` (mapeado para `5432` no container)
- **Redis**: `localhost:6379`

---

## 🧪 Testes Automatizados

Para executar os testes unitários e de integração:

- **Linux / macOS:**
  ```bash
  ./mvnw test
  ```

- **Windows:**
  ```powershell
  .\mvnw.cmd test
  ```

---

## 📦 Build do Projeto

Para compilar o projeto e gerar o executável JAR:

```bash
./mvnw clean package
```
O artefato final será gerado no diretório `target/ticketBookingSystem-0.0.1-SNAPSHOT.jar`.