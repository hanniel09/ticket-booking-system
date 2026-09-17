# Task Spec: 005 - Setup SpringDoc OpenAPI & Document Existing Controllers

## Status
- **Status:** Concluído

---

## 1. Context & Objective
Integrar a dependência oficial do SpringDoc OpenAPI UI ao projeto para expor a documentação interativa Swagger UI (`/swagger-ui.html` e `/v3/api-docs`).
Além da infraestrutura e liberação no Spring Security, enriquecer todos os Controllers já existentes com anotações declarativas (`@Tag`, `@Operation`, `@ApiResponse`, `@SecurityRequirement`) mantendo o padrão da API limpo e navegável.

---

## 2. Target Scope Boundaries

### Writable Files
- `pom.xml`
- `src/main/java/com/hanniel/ticketBookingSystem/config/openapi/OpenApiConfig.java` (Criar)
- `src/main/java/com/hanniel/ticketBookingSystem/config/security/SecurityConfig.java`
- `src/main/java/com/hanniel/ticketBookingSystem/controllers/auth/AuthController.java`
- `src/main/java/com/hanniel/ticketBookingSystem/controllers/billingAddress/BillingAddressController.java`
- `src/main/java/com/hanniel/ticketBookingSystem/controllers/event/EventController.java`
- `src/main/java/com/hanniel/ticketBookingSystem/controllers/order/OrderController.java`
- `src/main/java/com/hanniel/ticketBookingSystem/controllers/ticket/TicketController.java`
- `src/main/java/com/hanniel/ticketBookingSystem/controllers/ticket/TicketTypeController.java`
- `src/main/resources/application.properties`

### Read-Only Files
- `src/main/java/com/hanniel/ticketBookingSystem/domain/**`
- `src/main/java/com/hanniel/ticketBookingSystem/services/**`
- `src/main/java/com/hanniel/ticketBookingSystem/repositories/**`
- `src/main/resources/db/migration/**`

### Forbidden Zone
- NÃO alterar contratos de entrada/saída (DTOs/Records) existentes além de eventuais anotações de documentação `@Schema`.
- NÃO alterar a lógica interna de Services ou Regras de Negócio.

---

## 3. Technical Contract & Configurations

### Dependência Maven (`pom.xml`)
Adicionar o starter webmvc oficial compatível com Spring Boot 3.x/4.x:
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.8.5</version>
</dependency>
```

### Configuração de Propriedades (`application.properties`)
```properties
# SpringDoc / Swagger UI Configuration
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operations-sorter=method
springdoc.swagger-ui.tags-sorter=alpha
springdoc.swagger-ui.try-it-out-enabled=true
```

### Classe de Configuração Global (`OpenApiConfig.java`)
Localização: `src/main/java/com/hanniel/ticketBookingSystem/config/openapi/OpenApiConfig.java`
Deve definir o esquema de autenticação Bearer JWT para permitir testar rotas protegidas direto no navegador:
- Configurar `SecurityScheme` com tipo HTTP, scheme `bearer` e bearerFormat `JWT`.
- Definir informações da API: Título ("Ticket Booking System API"), Versão ("v1") e Descrição ("API de alta concorrência para emissão e reserva de ingressos").

### Liberação de Rotas Públicas (`SecurityConfig.java`)
Garantir que as rotas de documentação não exijam token JWT no `SecurityFilterChain`:
- `/v3/api-docs/**`
- `/swagger-ui/**`
- `/swagger-ui.html`

---

## 4. Implementation Checklist (Live State)
- [x] Passo 1: Adicionar `springdoc-openapi-starter-webmvc-ui` no `pom.xml`.
- [x] Passo 2: Configurar as propriedades do Swagger no `application.properties`.
- [x] Passo 3: Criar `OpenApiConfig.java` com as meta-informações e o esquema Bearer JWT.
- [x] Passo 4: Liberar os endpoints do Swagger UI e OpenAPI docs no `SecurityConfig.java`.
- [x] Passo 5: Documentar `AuthController.java`:
  - `@Tag(name = "Autenticação", description = "Endpoints de registro e autenticação de usuários")`
  - `@Operation` e `@ApiResponse` (200, 400, 401) nas rotas de login e registro.
- [x] Passo 6: Documentar `BillingAddressController.java`:
  - `@Tag(name = "Endereço de Cobrança", description = "Gerenciamento de dados fiscais do usuário")`
  - `@SecurityRequirement(name = "bearerAuth")`
- [x] Passo 7: Documentar `EventController.java`:
  - `@Tag(name = "Eventos", description = "Consulta e catálogo de eventos disponíveis")`
- [x] Passo 8: Documentar `OrderController.java`:
  - `@Tag(name = "Pedidos", description = "Checkout e simulação de pagamentos de ingressos")`
  - `@SecurityRequirement(name = "bearerAuth")`
- [x] Passo 9: Documentar `TicketController.java` e `TicketTypeController.java`:
  - `@Tag(name = "Ingressos", description = "Emissão e consulta de ingressos do usuário")`
  - `@Tag(name = "Tipos de Ingresso", description = "Gestão de lotes e estoque de ingressos")`

---

## 5. Verification Gate (Run before marking as DONE)
Execute os comandos de verificação sequencialmente no terminal:

```bash
# 1. Validar compilação sem erros de dependência ou anotações
./mvnw clean test-compile

# 2. Validar que os testes existentes continuam passando
./mvnw test
```