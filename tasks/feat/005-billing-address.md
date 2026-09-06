# Feature: Endereço de Cobrança (BillingAddress)

## Status
- **Status:** Concluído
- **Commits Relacionados:** `7cc9c6f`

---

## Escopo e Objetivos
Gerenciamento de dados fiscais e de faturamento do usuário (`BillingAddress`), atendendo à regra de negócio que torna o endereço de faturamento obrigatório para a finalização de compras de ingressos.

---

## Estrutura de Arquivos Criados

### 1. Domínio (`domain/billingAddress`)
- `BillingAddress.java`: Entidade com `id`, `userId`, `name`, `taxId` (CPF/CNPJ), `postalCode` (CEP), `street`, `number`, `complement`, `neighborhood`, `city`, `uf`, `phone`, `email` e flag `isShipping`.

### 2. DTOs e Mappers
- `BillingAddressRequestDTO.java`: Record com os campos cadastrais e fiscais requeridos.
- `BillingAddressResponseDTO.java`: Record com o endereço cadastrado retornado à API.
- `BillingAddressMapper.java`: Mapper via MapStruct entre entidade e DTOs.

### 3. Repositório e Serviço
- `BillingAddressRepository.java`: Operações JPA de persistência e consulta por usuário.
- `BillingAddressService.java`: Regras de negócio de cadastro, consulta, atualização e remoção de dados de endereço de cobrança.

### 4. Controller (`controllers/billingAddress`)
- `BillingAddressController.java`: Endpoints REST sob `/billing-address`:
  - `POST /billing-address`: Cadastra endereço de faturamento.
  - `GET /billing-address`: Lista endereços.
  - `GET /billing-address/{id}`: Consulta por UUID.
  - `PUT /billing-address/{id}`: Atualiza endereço de faturamento.
  - `DELETE /billing-address/{id}`: Remove endereço.

### 5. Testes Unitários
- `BillingAddressControllerTest.java`
- `BillingAddressServiceTest.java`
