# Sistema de Microsservicos Financeiro

Protótipo de backend financeiro distribuido para simular consulta e solicitacao de saque. O apresenta uma arquitetura hexagonal, separacao de responsabilidades, mensageria com RabbitMQ, cache/estado temporario com Redis, correlacao de requisicoes, idempotencia e testes unitarios de casos de uso.

## Stack

- Java 21
- Spring Boot 3.5.x
- Spring Web
- Spring AMQP
- Spring Data Redis
- Spring Actuator
- Bean Validation
- RabbitMQ
- Redis
- Docker Compose
- JUnit 5 e AssertJ

## Servicos

```text
.
├── consulta   # consumidor RabbitMQ que processa consultas e salva sessoes no Redis
├── saque      # consumidor RabbitMQ que valida sessoes e processa saques
├── gateway    # API HTTP publica e cliente request/reply para RabbitMQ
├── redis      # armazenamento temporario e idempotencia
└── rabbitmq   # broker, exchanges, filas e DLQs
```

## Arquitetura

Os servicos Spring Boot foram organizados em camadas inspiradas em Clean Architecture e Hexagonal Architecture. No gateway, a integracao com RabbitMQ tambem fica atras de uma porta de saida, implementada por um adapter de mensageria.

```text
domain       # modelo e regras de negocio sem dependencia de framework
application  # casos de uso, comandos, resultados e portas
adapter/in   # entrada HTTP ou RabbitMQ
adapter/out  # Redis, RabbitMQ e outras integracoes
config       # configuracoes Spring
```

Essa organizacao deixa o dominio testavel sem infraestrutura e evita que controllers/listeners concentrem regra de negocio.

## Fluxo

1. `POST /consult` recebe `identifier`, `agency` e `account`.
2. O gateway cria ou propaga `Correlation-Id` e publica em `exchange-consult-rabbit`.
3. O servico `consulta` consome `queue-consult-rabbit`, cria uma sessao aprovada e salva no Redis.
4. O gateway recebe a resposta em uma fila temporaria `reply-consult-rabbit-{identifier}`.
5. `POST /{identifier}/cash-withdrawal` recebe `amount` e `password`.
6. O gateway publica em `exchange-cash-withdrawal-rabbit`, propagando `Correlation-Id` e o `Idempotency-Key` obrigatorio.
7. O servico `saque` valida a sessao no Redis, reserva a chave de idempotencia e retorna o saque aprovado.

## Robustez adicionada

- Java 21 e Spring Boot 3.5.x.
- DTOs HTTP com Bean Validation.
- Controllers REST com tratamento centralizado de erro.
- `Correlation-Id` propagado entre HTTP e RabbitMQ.
- `Idempotency-Key` obrigatorio no fluxo de saque.
- Redis isolado atras de ports de saida.
- Controllers e listeners isolados como adapters de entrada.
- Integracoes com RabbitMQ e Redis isoladas como adapters de saida atras de ports.
- DLQs para consulta e saque no RabbitMQ.
- Actuator habilitado para health, info e metricas.
- Testes unitarios dos casos de uso sem depender de Redis/RabbitMQ.

## Execucao

```bash
docker-compose up -d
```

Endpoints:

- `POST http://localhost:9991/consult`
- `POST http://localhost:9991/{identifier}/cash-withdrawal` com header obrigatorio `Idempotency-Key`
- Swagger UI do gateway: `http://localhost:9991/swagger-ui.html`
- RabbitMQ Management: `http://localhost:15672`

Exemplo de consulta:

```bash
curl -X POST http://localhost:9991/consult \
  -H "Content-Type: application/json" \
  -H "Correlation-Id: demo-001" \
  -d "{\"identifier\":\"abc-123\",\"agency\":\"0001\",\"account\":\"12345-6\"}"
```

Exemplo de saque:

```bash
curl -X POST http://localhost:9991/abc-123/cash-withdrawal \
  -H "Content-Type: application/json" \
  -H "Correlation-Id: demo-001" \
  -H "Idempotency-Key: withdrawal-demo-001" \
  -d "{\"amount\":100.00,\"password\":\"1234\"}"
```

## Testes

```bash
cd gateway && ./mvnw test
cd ../consulta && ./mvnw test
cd ../saque && ./mvnw test
```
