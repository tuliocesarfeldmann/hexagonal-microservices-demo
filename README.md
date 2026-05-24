# Financial Microservices System

Distributed financial backend prototype that simulates account consult and withdrawal requests. The project demonstrates hexagonal architecture, responsibility separation, RabbitMQ messaging, temporary Redis state, request correlation, idempotency, and unit tests for application use cases.

## Stack

- Java 21 LTS
- Spring Boot 3.5.x
- Spring Web
- Spring AMQP
- Spring Data Redis
- Spring Actuator
- Bean Validation
- RabbitMQ
- Redis
- Docker Compose
- JUnit 5 and AssertJ

## Services

```text
.
├── consult     # RabbitMQ consumer that processes consult requests and stores sessions in Redis
├── withdrawal  # RabbitMQ consumer that validates sessions and processes withdrawals
├── gateway     # public HTTP API and RabbitMQ request/reply client
├── redis       # temporary storage and idempotency
└── rabbitmq    # broker, exchanges, queues, and DLQs
```

## Architecture

The Spring Boot services are organized in layers inspired by Clean Architecture and Hexagonal Architecture. In the gateway, the RabbitMQ integration is also behind an outbound port implemented by a messaging adapter.

```text
domain       # model and business rules without framework dependencies
application  # use cases, commands, results, and ports
adapter/in   # HTTP or RabbitMQ input
adapter/out  # Redis, RabbitMQ, and other integrations
config       # Spring configuration
```

This organization keeps the domain testable without infrastructure and prevents controllers/listeners from concentrating business rules.

## Flow

1. `POST /consult` receives `identifier`, `agency`, and `account`.
2. The gateway creates or propagates `Correlation-Id` and publishes to `exchange-consult-rabbit`.
3. The `consult` service consumes `queue-consult-rabbit`, creates an approved session, and stores it in Redis.
4. The gateway receives the response in a temporary queue named `reply-consult-rabbit-{identifier}`.
5. `POST /{identifier}/cash-withdrawal` receives `amount` and `password`.
6. The gateway publishes to `exchange-cash-withdrawal-rabbit`, propagating `Correlation-Id` and the required `Idempotency-Key`.
7. The `withdrawal` service validates the session in Redis, reserves the idempotency key, and returns the approved withdrawal.

## Added Robustness

- Java 21 and Spring Boot 3.5.x.
- HTTP DTOs with Bean Validation.
- REST controllers with centralized error handling.
- `Correlation-Id` propagated between HTTP and RabbitMQ.
- Required `Idempotency-Key` in the withdrawal flow.
- Redis isolated behind outbound ports.
- Controllers and listeners isolated as input adapters.
- RabbitMQ and Redis integrations isolated as outbound adapters behind ports.
- DLQs for consult and withdrawal in RabbitMQ.
- Actuator enabled for health, info, and metrics.
- Unit tests for use cases without requiring Redis/RabbitMQ.

## Running

```bash
docker-compose up -d
```

Endpoints:

- `POST http://localhost:9991/consult`
- `POST http://localhost:9991/{identifier}/cash-withdrawal` with required `Idempotency-Key` header
- Gateway Swagger UI: `http://localhost:9991/swagger-ui.html`
- RabbitMQ Management: `http://localhost:15672`

Consult example:

```bash
curl -X POST http://localhost:9991/consult \
  -H "Content-Type: application/json" \
  -H "Correlation-Id: demo-001" \
  -d "{\"identifier\":\"abc-123\",\"agency\":\"0001\",\"account\":\"12345-6\"}"
```

Withdrawal example:

```bash
curl -X POST http://localhost:9991/abc-123/cash-withdrawal \
  -H "Content-Type: application/json" \
  -H "Correlation-Id: demo-001" \
  -H "Idempotency-Key: withdrawal-demo-001" \
  -d "{\"amount\":100.00,\"password\":\"1234\"}"
```

## Tests

```bash
cd gateway && ./mvnw test
cd ../consult && ./mvnw test
cd ../withdrawal && ./mvnw test
```
