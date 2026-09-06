# Feature: Utilitário de Datas (DateHelper)

## Status
- **Status:** Concluído
- **Commits Relacionados:** `efada38`, `500d089`, `a6a65e6`

---

## Escopo e Objetivos
Padronização da conversão e serialização de datas no formato brasileiro `DD/MM/YYYY` em payloads JSON e instâncias de `ZonedDateTime` no domínio da aplicação.

---

## Estrutura de Arquivos Criados

### 1. Helper (`helper/date`)
- `DateHelper.java`: Componente com métodos estáticos/instanciáveis de conversão:
  - Conversão de `String` (formato `dd/MM/yyyy`) para `ZonedDateTime` com fuso horário padrão (`ZoneId.systemDefault()`).
  - Conversão de `ZonedDateTime` para `String` formatada.
  - Validação e tratamento de formatação em operações de parsing.

### 2. Integração com Mappers
- Integrado diretamente ao `EventMapper.java` para transformação automática das datas em `EventRequestDTO` e `EventResponseDTO`.

### 3. Testes Unitários
- `DateHelperTest.java`: Validação de cenários de conversão, formatação e tratamento de exceções de parsing de data.
