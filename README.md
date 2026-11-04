# Fiverr Order Service (`fiverr-order-service`)

Production-grade **Order Lifecycle & State Machine Microservice** for Fiverr Club, built with **Java 21**, **Spring Boot 3.3.5**, **Domain-Driven Design (DDD)**, and **Hexagonal Architecture (Ports & Adapters)**.

## Architecture & Module Structure

```
club.fiverr.order
├── presentation/rest/
│   ├── OrderController.java
│   ├── request/                # PlaceOrderRequest, DeliverOrderRequest, RequestRevisionRequest, CompleteOrderRequest
│   ├── response/               # OrderResponse
│   └── mapper/                 # OrderRestMapper
├── application/
│   ├── dto/
│   │   ├── command/            # PlaceOrderCommand, DeliverOrderCommand, RequestRevisionCommand, CompleteOrderCommand
│   │   └── result/             # OrderResult
│   ├── mapper/                 # OrderResultMapper
│   ├── port/
│   │   ├── in/                 # PlaceOrderUseCase, DeliverOrderUseCase, RequestRevisionUseCase, CompleteOrderUseCase
│   │   └── out/                # EscrowPaymentPort, OrderEventPublisherPort (External Services ONLY)
│   └── usecase/                # PlaceOrderUseCaseImpl, DeliverOrderUseCaseImpl, RequestRevisionUseCaseImpl, CompleteOrderUseCaseImpl
├── domain/
│   ├── model/                  # Order, OrderStatus (CREATED -> IN_PROGRESS -> DELIVERED -> REVISION_REQUESTED / COMPLETED / CANCELLED / DISPUTED)
│   ├── repository/             # OrderRepository (Domain Repository Interface)
│   └── exception/              # OrderNotFoundException, InvalidOrderStateException
└── infrastructure/
    ├── persistence/
    │   ├── entity/             # OrderEntity
    │   ├── mapper/             # OrderPersistenceMapper
    │   └── repository/         # JpaOrderRepository, OrderJpaRepository
    └── adapter/                # EscrowPaymentAdapter, OrderEventPublisherAdapter
```

## Order State Machine Transitions
- `CREATED` → `IN_PROGRESS` (upon escrow hold)
- `IN_PROGRESS` / `REVISION_REQUESTED` → `DELIVERED` (when seller submits deliverables)
- `DELIVERED` → `REVISION_REQUESTED` (when buyer requests a revision within `maxRevisions` quota)
- `DELIVERED` → `COMPLETED` (when buyer accepts delivery and escrow is released)
- `CREATED` / `IN_PROGRESS` → `CANCELLED`
- `IN_PROGRESS` / `DELIVERED` / `REVISION_REQUESTED` → `DISPUTED`

## REST Endpoints (Port `8084`)
- `POST /api/v1/orders` — Place a new order & hold escrow
- `POST /api/v1/orders/{orderId}/deliver` — Seller delivers order work
- `POST /api/v1/orders/{orderId}/revisions` — Buyer requests a revision
- `POST /api/v1/orders/{orderId}/complete` — Buyer completes order & releases escrow
- `GET /actuator/health/liveness` — Kubernetes liveness probe
- `GET /actuator/health/readiness` — Kubernetes readiness probe

## Running Tests & Building

```bash
JAVA_HOME=/opt/homebrew/opt/openjdk@21 mvn test
```
