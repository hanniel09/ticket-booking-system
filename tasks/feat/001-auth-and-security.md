# Feature: Autenticação e Segurança (JWT + Spring Security)

## Status
- **Status:** Concluído
- **Commits Relacionados:** `b8d1234`

---

## Escopo e Objetivos
Implementação de autenticação stateless via JSON Web Tokens (JWT), controle de acesso por roles (`USER`, `ADMIN`) e criptografia de senhas com BCrypt.

---

## Estrutura de Arquivos Criados

### 1. Domínio (`domain/user`)
- `User.java`: Entidade de usuário implementando `UserDetails`, mapeada para tabela `users`, com ID UUID, email único, senha criptografada e role.
- `UserRole.java`: Enum com papéis (`ADMIN`, `USER`).

### 2. DTOs (`dtos/auth`)
- `AuthenticationDTO.java`: Record com `email` e `password`.
- `RegisterDTO.java`: Record para cadastro de novos usuários com definição de `role`.
- `LoginResponseDTO.java`: Record com o token JWT retornado no login.

### 3. Repositório e Serviços
- `UserRepository.java`: Repositório Spring Data JPA com busca por email (`findByEmail`).
- `CustomUserDetailsService.java`: Implementação de `UserDetailsService` para carga de credenciais.
- `TokenService.java`: Geração e validação de tokens JWT usando algoritmo HMAC256 com tempo de expiração.
- `AuthService.java`: Regras de negócio de autenticação e registro com validação de duplicidade.

### 4. Filtro e Configuração de Segurança (`config/security`)
- `SecurityFilter.java`: `OncePerRequestFilter` que extrai o Bearer token do header `Authorization`, valida e injeta a autenticação no `SecurityContextHolder`.
- `SecurityConfig.java`: Configuração de `SecurityFilterChain` sem estado (`SessionCreationPolicy.STATELESS`), liberação pública de `/auth/**` e proteção das demais rotas, além de expor os beans `AuthenticationManager` e `PasswordEncoder` (`BCryptPasswordEncoder`).

### 5. Controller (`controllers/auth`)
- `AuthController.java`:
  - `POST /auth/login`: Autentica usuário e retorna JWT token.
  - `POST /auth/register`: Registra novo usuário com senha criptografada.

### 6. Testes Unitários
- `AuthControllerTest.java`
- `AuthServiceTest.java`
- `CustomUserDetailsServiceTest.java`
- `TokenServiceTest.java`
