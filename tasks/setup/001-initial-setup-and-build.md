# Setup: Inicialização do Projeto e Build

## Status
- **Status:** Concluído
- **Data de Implementação:** Inicial / Atualizado para Java 25
- **Commits Relacionados:** `a5e1b85`, `4691e99`

---

## Escopo e Objetivos
Configuração inicial do projeto Spring Boot com suporte às especificações mais recentes do Java e Maven, garantindo uma base modular e moderna para o sistema de venda de ingressos.

---

## Componentes Configurados

### 1. Ambiente e Linguagem
- **Java 25:** Configuração no `pom.xml` (`<java.version>25</java.version>`).
- **Spring Boot 4.0.1:** Utilização da versão mais recente como parent starter (`spring-boot-starter-parent`).

### 2. Gerenciamento de Dependências (`pom.xml`)
- `spring-boot-starter-webmvc`
- `spring-boot-starter-data-jpa`
- `spring-boot-starter-security`
- `spring-boot-starter-validation`
- `spring-boot-starter-data-redis`
- `org.postgresql:postgresql`
- `org.flywaydb:flyway-database-postgresql` (11.11.2)
- `com.auth0:java-jwt` (4.4.0)
- `org.projectlombok:lombok`
- `org.mapstruct:mapstruct` (1.6.2)

### 3. Plugins e Processadores de Anotações
- **`maven-compiler-plugin`**: Configurado com `annotationProcessorPaths` para integração perfeita entre:
  - Lombok (`lombok`)
  - Lombok-MapStruct binding (`lombok-mapstruct-binding:0.2.0`)
  - MapStruct Processor (`mapstruct-processor:1.6.2`)
- **`spring-boot-maven-plugin`**: Configurado com exclusão do Lombok no empacotamento final.

### 4. Maven Wrapper
- Wrappers `mvnw` (Unix) e `mvnw.cmd` (Windows) configurados sob `.mvn/wrapper/maven-wrapper.properties`.

---

## Verificação e Testes
- Build e compilação validados via Maven.
- Processadores de anotação geram mappers e código boilerplate em tempo de compilação sem conflitos.
