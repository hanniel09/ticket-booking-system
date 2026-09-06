# Setup: Configuração do Redis e Pool Lettuce

## Status
- **Status:** Concluído
- **Commits Relacionados:** `aab2858`

---

## Escopo e Objetivos
Configuração do Redis como camada de cache e suporte para controle de concorrência e bloqueios distribuídos com pool de conexões otimizado via Lettuce.

---

## Componentes Configurados

### 1. Configurações de Conexão e Pool (`application.properties`)
- `spring.data.redis.host=localhost`
- `spring.data.redis.port=6379`
- `spring.data.redis.password=supersecretpassword`
- `spring.data.redis.lettuce.pool.max-active=16`
- `spring.data.redis.lettuce.pool.max-idle=8` (mapeado no pool)
- `spring.data.redis.lettuce.pool.min-idle=2`
- `spring.data.redis.lettuce.pool.max-wait=2000ms`
- `spring.data.redis.lettuce.shutdown-timeout=100ms`

### 2. Classe de Configuração (`RedisConfig.java`)
Localização: `src/main/java/com/hanniel/ticketBookingSystem/config/redis/RedisConfig.java`
- Configuração do `RedisConnectionFactory` com `RedisStandaloneConfiguration`.
- Configuração do Bean `RedisTemplate<String, Object>` com:
  - `StringRedisSerializer` para serialização de chaves (`keySerializer` e `hashKeySerializer`).
  - `GenericJackson2JsonRedisSerializer` para serialização de valores (`valueSerializer` e `hashValueSerializer`).
