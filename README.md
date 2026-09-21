# Microservice Dependency Impact Engine

> **A production-oriented distributed systems platform for discovering microservice dependencies, building a live dependency graph, and analyzing change impact and failure blast radius.**

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3-brightgreen)
![Kafka](https://img.shields.io/badge/Apache%20Kafka-event--driven-black)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-database-blue)
![Redis](https://img.shields.io/badge/Redis-cache-red)
![React](https://img.shields.io/badge/React-TypeScript-61DAFB)
![Docker](https://img.shields.io/badge/Docker-containerized-2496ED)
![OpenTelemetry](https://img.shields.io/badge/OpenTelemetry-observability-7B61FF)

---

## 1. Overview

Modern applications are increasingly built using microservices.

A single business operation may involve:

```text
Order Service
      ↓
Payment Service
      ↓
Payment Database
      ↓
Kafka
      ↓
Notification Service
      ↓
Email Provider
```

The problem is that developers often do not have a complete view of these relationships.

A seemingly small change to one service can potentially affect:

- Downstream services
- REST APIs
- Kafka consumers
- Databases
- External APIs
- Critical user journeys
- Other engineering teams

The **Microservice Dependency Impact Engine** addresses this problem by combining static dependency information with runtime telemetry to create a dependency intelligence platform.

---

# 2. Core Problem

The platform answers questions such as:

### Before making a change

> "If I change `payment-service`, what can be affected?"

### During an incident

> "If `payment-service` is failing, which services and user journeys are likely to be affected?"

### During API changes

> "If I remove this API field, which consumers could break?"

### During Kafka changes

> "Which consumers depend on this event?"

### During deployment

> "Did this deployment introduce downstream latency or failures?"

---

# 3. Core Idea

The central concept is a **live dependency graph**.

```text
                 ┌──────────────────┐
                 │   Order Service  │
                 └────────┬─────────┘
                          │ REST
                          ▼
                 ┌──────────────────┐
                 │ Payment Service  │
                 └───────┬──────────┘
                         │
                  ┌──────┴───────┐
                  │              │
                  ▼              ▼
             PostgreSQL        Kafka
                                 │
                                 ▼
                         Notification Service
```

The graph contains different types of nodes and relationships.

---

# 4. Dependency Graph

## Nodes

The platform supports:

```text
SERVICE
API
DATABASE
KAFKA_TOPIC
CACHE
QUEUE
EXTERNAL_API
```

## Relationships

```text
CALLS
PRODUCES
CONSUMES
READS_FROM
WRITES_TO
DEPENDS_ON
USES
```

Each relationship can contain metadata such as:

```text
Protocol
Endpoint
Kafka Topic
Latency
Error Rate
Criticality
Failure Behavior
Confidence
First Seen
Last Seen
Environment
Owner
```

---

# 5. Static + Runtime Dependency Discovery

The platform does not rely on a single dependency source.

It combines:

```text
                Dependency Sources

        ┌───────────────┐
        │ OpenAPI Specs │
        └───────┬───────┘
                │
        ┌───────▼───────┐
        │ Kafka Metadata│
        └───────┬───────┘
                │
        ┌───────▼────────┐
        │ Database Meta  │
        └───────┬────────┘
                │
        ┌───────▼────────┐
        │ Kubernetes Meta│
        └───────┬────────┘
                │
        ┌───────▼────────┐
        │ OpenTelemetry  │
        └───────┬────────┘
                │
                ▼
       Dependency Collector
                │
                ▼
               Kafka
                │
                ▼
       Dependency Graph
```

### Why combine both?

Static information tells us:

> "This dependency is configured or potentially possible."

Runtime information tells us:

> "This dependency actually occurred."

Combining both provides better dependency visibility.

---

# 6. Major Capabilities

## 6.1 Dependency Discovery

Automatically discover relationships between:

- Microservices
- REST APIs
- Kafka producers/consumers
- Databases
- External APIs
- Runtime services

---

## 6.2 Live Dependency Graph

Explore architecture through an interactive graph.

Example:

```text
Checkout
   │
   ├── Payment
   │      ├── Payment DB
   │      └── Kafka
   │             └── Notification
   │
   └── Inventory
          └── Inventory DB
```

Graph exploration supports:

- Upstream dependencies
- Downstream dependencies
- Depth limits
- Environment filtering
- Dependency-type filtering
- Criticality filtering
- Team filtering
- Search
- Node expansion

---

# 7. Change Impact Analysis

The most important capability is change-impact analysis.

Example:

```text
Changed Service:
payment-service
```

The engine traverses the dependency graph.

```text
payment-service
      │
      ├── checkout-service
      │
      ├── order-service
      │
      └── payment-events
              │
              ├── notification-service
              └── analytics-service
```

The result identifies:

```text
Direct Consumers
Indirect Consumers
Affected APIs
Affected Kafka Topics
Affected Databases
Affected Teams
Dependency Paths
Critical Dependencies
Runtime Evidence
```

---

# 8. Failure Blast Radius

The same graph can be used during runtime incidents.

Example:

```text
Payment Service
      │
      │ FAILURE
      ▼
Checkout Service
      │
      ▼
Order Service
```

The platform determines which downstream components may be affected.

The result includes:

```text
Affected Services
Affected APIs
Dependency Paths
Failure Behavior
Criticality
Runtime Evidence
Potential User Journeys
```

---

# 9. Fail-Open vs Fail-Close

The platform models dependency failure behavior.

### Fail-Close

A dependency failure prevents the main operation from completing.

Example:

```text
Checkout
   ↓
Payment
   ↓
Payment fails
   ↓
Checkout fails
```

### Fail-Open

The main operation can continue with degraded functionality.

Example:

```text
Product Page
   ↓
Recommendation Service
   ↓
Recommendation fails
   ↓
Product Page continues
```

This information helps determine potential impact severity.

---

# 10. API Breaking Change Detection

The platform can compare API definitions.

Example:

```text
Before:

GET /users/{id}

{
    "id": 123,
    "name": "Arman",
    "email": "..."
}
```

Changed version:

```text
{
    "id": 123,
    "name": "Arman"
}
```

The platform can identify consumers that may depend on the removed field.

---

# 11. Kafka Dependency Analysis

Kafka relationships are represented in the dependency graph.

Example:

```text
Order Service
      │
      │ PRODUCES
      ▼
order-created
      │
      ├───────────────┐
      ▼               ▼
Notification       Analytics
 Service             Service
```

The platform can answer:

> "If the `order-created` event schema changes, which consumers may be affected?"

---

# 12. Database Dependency Analysis

Database relationships are also tracked.

Example:

```text
Order Service
      │
      ├── READS
      ▼
orders table
```

A schema change can therefore be connected to potentially affected services.

---

# 13. Runtime Intelligence

The platform integrates with OpenTelemetry.

Runtime observations can contain:

```text
Trace ID
Span ID
Source Service
Target Service
HTTP Method
Endpoint
Latency
Status Code
Timestamp
```

This information can be correlated with the dependency graph.

---

# 14. Deployment Correlation

Future versions can connect:

```text
GitHub PR
    ↓
Deployment
    ↓
Service
    ↓
Dependency Graph
    ↓
Runtime Metrics
```

Example:

```text
Deployment:
payment-service v2.4

       ↓

p95 latency increased

       ↓

checkout-service latency increased

       ↓

Dependency graph confirms:

payment → checkout
```

This creates a connection between code changes and runtime behavior.

---

# 15. Architecture

High-level architecture:

```text
                         ┌───────────────────────┐
                         │     React Dashboard   │
                         └───────────┬───────────┘
                                     │
                              HTTPS / WebSocket
                                     │
                                     ▼
                         ┌───────────────────────┐
                         │      API Gateway      │
                         └───────────┬───────────┘
                                     │
              ┌──────────────────────┼──────────────────────┐
              │                      │                      │
              ▼                      ▼                      ▼
       ┌──────────────┐      ┌──────────────┐      ┌──────────────┐
       │   Service    │      │ Dependency   │      │    Impact    │
       │   Registry   │      │    Graph     │      │   Analysis   │
       └──────────────┘      └──────┬───────┘      └──────┬───────┘
                                    │                     │
                                    └──────────┬──────────┘
                                               ▼
                                         ┌───────────┐
                                         │   Kafka   │
                                         └─────┬─────┘
                                               │
                         ┌─────────────────────┼──────────────────┐
                         │                     │                  │
                         ▼                     ▼                  ▼
                 Dependency Collector   Runtime Analysis   Notification
                         │                     │
                         └──────────┬──────────┘
                                    │
                       ┌────────────┴────────────┐
                       ▼                         ▼
                 PostgreSQL                    Redis
                       │
                       ▼
                  Dependency
                     Graph
```

---

# 16. Backend Services

The backend is divided into logical services.

| Service                  | Responsibility                         |
| ------------------------ | -------------------------------------- |
| API Gateway              | Routing, authentication, rate limiting |
| Service Registry         | Services, teams, environments          |
| Dependency Collector     | Dependency discovery                   |
| Dependency Graph Service | Dependency storage and traversal       |
| Impact Analysis Service  | Change-impact calculation              |
| Runtime Analysis Service | Telemetry and incident analysis        |
| Notification Service     | Alerts and notifications               |

---

# 17. Technology Stack

## Backend

```text
Java 21
Spring Boot 3
Spring Security
Spring Data JPA
Hibernate
Spring Kafka
Spring Validation
Spring Actuator
Maven
```

## Database

```text
PostgreSQL
Flyway
```

## Messaging

```text
Apache Kafka
```

## Cache

```text
Redis
```

## Observability

```text
OpenTelemetry
Micrometer
Spring Actuator
```

## Frontend

```text
React
TypeScript
TanStack Query
WebSockets
```

## Infrastructure

```text
Docker
Docker Compose
Kubernetes
AWS
GitHub Actions
```

## Testing

```text
JUnit 5
Mockito
MockMvc
Testcontainers
```

---

# 18. Database

PostgreSQL acts as the initial system of record.

Core tables include:

```text
teams
services
service_environments
apis
dependencies
kafka_topics
kafka_service_bindings
databases
service_database_dependencies
external_dependencies
runtime_observations
impact_analyses
impact_components
incidents
audit_logs
processed_events
```

The central relationship is stored in:

```text
dependencies
```

---

# 19. Dependency Relationship

Conceptually:

```text
source_service
       │
       │ dependency
       ▼
target_service
```

Example:

```text
Order Service
       │
       │ REST
       ▼
Payment Service
```

The relationship stores metadata such as:

```text
dependency_type
protocol
endpoint
environment
criticality
failure_behavior
confidence
first_seen_at
last_seen_at
```

---

# 20. Kafka Topics

Core topics:

```text
dependency-events
runtime-events
service-events
impact-analysis-events
incident-events
notification-events
```

Example event:

```json
{
  "eventId": "evt-123",
  "eventType": "DEPENDENCY_DISCOVERED",
  "eventVersion": 1,
  "timestamp": "2026-09-20T12:30:00Z",
  "source": "dependency-collector",
  "correlationId": "req-123",
  "payload": {
    "sourceService": "order-service",
    "targetService": "payment-service",
    "dependencyType": "REST"
  }
}
```

---

# 21. Graph Traversal

The initial graph engine uses bounded BFS traversal.

Example:

```text
payment-service
       │
       ├── checkout-service
       │       │
       │       └── order-service
       │
       └── notification-service
```

With:

```text
depth = 2
```

the engine will only traverse the configured number of levels.

This prevents expensive unbounded graph traversal.

---

# 22. Impact Analysis Flow

```text
Developer
    │
    │ Change Analysis Request
    ▼
Impact API
    │
    ▼
Impact Engine
    │
    ├── Load Service
    │
    ├── Load Dependencies
    │
    ├── Traverse Graph
    │
    ├── Resolve APIs
    │
    ├── Resolve Kafka
    │
    ├── Resolve Teams
    │
    └── Evaluate Criticality
            │
            ▼
       Impact Report
```

---

# 23. REST API

Base URL:

```text
/api/v1
```

Examples:

```http
GET /api/v1/services
GET /api/v1/services/{id}
GET /api/v1/services/{id}/dependencies
GET /api/v1/services/{id}/dependents
GET /api/v1/graph/services/{id}
GET /api/v1/graph/services/{id}/downstream
POST /api/v1/impact/analyze
GET /api/v1/impact/{id}
GET /api/v1/impact/{id}/report
GET /api/v1/incidents
GET /api/v1/dashboard/summary
```

---

# 24. Asynchronous Impact Analysis

Large impact calculations should be asynchronous.

Request:

```http
POST /api/v1/impact/analyze
```

Response:

```http
202 Accepted
```

Example:

```json
{
  "success": true,
  "data": {
    "analysisId": "impact-123",
    "status": "PENDING"
  }
}
```

The frontend can then monitor:

```text
PENDING
   ↓
RUNNING
   ↓
COMPLETED
```

---

# 25. Authentication

Authentication uses JWT.

Roles:

```text
ADMIN
ARCHITECT
DEVELOPER
SRE
VIEWER
```

Authorization is applied at API and service boundaries.

---

# 26. Caching

Redis is used for:

```text
Frequently accessed services
Dependency queries
Graph subgraphs
Impact reports
Temporary analysis state
```

Cache strategy:

```text
Request
   ↓
Redis
   │
   ├── HIT → Return
   │
   └── MISS
         ↓
     PostgreSQL
         ↓
       Redis
```

---

# 27. Resilience

The platform uses:

```text
Timeouts
Retries
Circuit Breakers
Idempotency
Dead Letter Queues
Health Checks
```

The objective is to prevent failures in one component from unnecessarily cascading through the platform.

---

# 28. Security

Security controls include:

```text
JWT Authentication
RBAC
HTTPS
Input Validation
Rate Limiting
Secret Management
Audit Logging
PII/Secret Redaction
Least Privilege
```

Sensitive credentials are never committed to Git.

---

# 29. Observability

Every backend service exposes:

```text
Health
Metrics
Logs
Traces
```

Important metrics include:

```text
HTTP latency
HTTP errors
Kafka consumer lag
Kafka processing time
Dependency ingestion rate
Impact analysis duration
Graph traversal duration
Redis cache hit rate
Database query latency
```

---

# 30. Testing Strategy

Testing layers:

```text
Unit Tests
     ↓
Controller/API Tests
     ↓
Integration Tests
     ↓
Kafka Tests
     ↓
Database Tests
     ↓
End-to-End Tests
```

Testcontainers provides real:

```text
PostgreSQL
Kafka
Redis
```

containers for integration testing.

---

# 31. Docker

All major components can run through Docker Compose.

```text
docker-compose.yml

PostgreSQL
Kafka
Redis
Backend Services
Frontend
```

Start locally:

```bash
docker compose up -d
```

Stop:

```bash
docker compose down
```

---

# 32. Kubernetes

Production-oriented deployment can use:

```text
Kubernetes
```

with:

```text
Deployment
Service
ConfigMap
Secret
Health Probes
Horizontal Pod Autoscaler
```

Stateless services can scale independently.

---

# 33. CI/CD

GitHub Actions pipeline:

```text
Pull Request
     ↓
Compile
     ↓
Unit Tests
     ↓
Integration Tests
     ↓
Static Analysis
     ↓
Docker Build
     ↓
Security Scan
     ↓
Image Push
     ↓
Deployment
```

Failed tests should block deployment.

---

# 34. Local Development

### Requirements

```text
Java 21
Maven
Node.js
Docker
Docker Compose
Git
```

### Clone

```bash
git clone https://github.com/<your-username>/microservice-dependency-impact-engine.git

cd microservice-dependency-impact-engine
```

### Start infrastructure

```bash
docker compose up -d postgres kafka redis
```

### Start backend

```bash
cd backend
./mvnw spring-boot:run
```

### Start frontend

```bash
cd frontend
npm install
npm run dev
```

---

# 35. Project Structure

```text
microservice-dependency-impact-engine/
│
├── backend/
│   ├── api-gateway/
│   ├── service-registry/
│   ├── dependency-collector/
│   ├── dependency-graph-service/
│   ├── impact-analysis-service/
│   ├── runtime-analysis-service/
│   ├── notification-service/
│   └── common/
│
├── frontend/
│
├── infrastructure/
│   ├── docker/
│   └── kubernetes/
│
├── docs/
│
├── scripts/
│
├── docker-compose.yml
│
├── README.md
│
└── .github/
    └── workflows/
```

---

# 36. Documentation

Detailed documentation is maintained separately.

| Document                       | Description                  |
| ------------------------------ | ---------------------------- |
| `PRD.md`                       | Product requirements         |
| `SYSTEM_DESIGN.md`             | System architecture          |
| `DATABASE_DESIGN.md`           | Database architecture        |
| `API_SPECIFICATION.md`         | REST/WebSocket APIs          |
| `TECHNICAL_IMPLEMENTATION.md`  | Implementation details       |
| `UI_UX_DESIGN.md`              | Frontend and UX architecture |
| `SECURITY_DESIGN.md`           | Security architecture        |
| `TESTING_STRATEGY.md`          | Testing approach             |
| `KAFKA_EVENT_DRIVEN_DESIGN.md` | Kafka architecture           |
| `OBSERVABILITY_DESIGN.md`      | Logging, metrics and tracing |
| `DEPLOYMENT_DEVOPS.md`         | Docker/Kubernetes/CI/CD      |
| `LLD.md`                       | Low-level design             |
| `DEVELOPMENT_ROADMAP.md`       | Implementation roadmap       |

---

# 37. MVP Scope

The first version focuses on the core engineering problem.

### Included

```text
✓ Java 21
✓ Spring Boot 3
✓ REST APIs
✓ PostgreSQL
✓ Kafka
✓ Redis
✓ JWT/RBAC
✓ Dependency discovery
✓ Dependency graph
✓ BFS traversal
✓ Change impact analysis
✓ Basic runtime observations
✓ React dashboard
✓ OpenTelemetry
✓ JUnit
✓ Mockito
✓ Testcontainers
✓ Docker Compose
✓ GitHub Actions
```

### Deferred

```text
○ Advanced ML prediction
○ Full GitHub PR automation
○ Dedicated graph database
○ Advanced anomaly detection
○ Service mesh
○ Multi-region deployment
○ Complex AI root-cause analysis
```

---

# 38. What Makes This Project Different

This is not simply:

```text
CRUD + Authentication + Dashboard
```

The core engineering problem involves:

```text
Distributed Systems
        +
Event-Driven Architecture
        +
Graph Algorithms
        +
Runtime Telemetry
        +
Change Impact Analysis
        +
Microservice Architecture
```

The important capability is connecting these concepts.

---

# 39. Example End-to-End Scenario

Suppose:

```text
checkout-service
       ↓
payment-service
       ↓
payment-events
       ↓
notification-service
```

A developer changes:

```text
payment-service
```

The platform performs:

```text
1. Identify changed service
2. Load dependency graph
3. Find direct consumers
4. Traverse downstream dependencies
5. Resolve REST relationships
6. Resolve Kafka consumers
7. Resolve criticality
8. Resolve owning teams
9. Check runtime evidence
10. Generate impact report
```

Result:

```text
Potentially affected:

checkout-service
notification-service

Affected Kafka topic:

payment-events

Dependency path:

payment-service
    ↓
payment-events
    ↓
notification-service

Risk factors:

- Critical dependency
- Fail-close relationship
- Runtime traffic observed
```

---

# 40. Engineering Challenges Demonstrated

This project intentionally demonstrates practical backend engineering challenges.

### Challenge 1 — Dependency Discovery

No single source contains every dependency.

Solution:

```text
Static Sources
+
Runtime Telemetry
```

---

### Challenge 2 — Graph Scalability

Large graphs cannot be loaded completely for every request.

Solution:

```text
Bounded Traversal
+
Filtering
+
Pagination
+
Caching
```

---

### Challenge 3 — Event Reliability

Kafka events can be duplicated or fail processing.

Solution:

```text
Idempotency
+
Retries
+
DLQ
+
Processed Event Tracking
```

---

### Challenge 4 — Runtime Visibility

Static architecture does not always represent actual runtime behavior.

Solution:

```text
OpenTelemetry
+
Runtime Observations
+
Dependency Graph
```

---

### Challenge 5 — Failure Propagation

A failed service may affect multiple downstream components.

Solution:

```text
Dependency Graph
+
Failure Behavior
+
Runtime Evidence
```

---

# 41. Future Enhancements

Potential future capabilities:

```text
GitHub Pull Request Integration
OpenAPI Schema Diff
Kafka Schema Registry Integration
Database Schema Diff
Deployment Correlation
Architecture Risk Dashboard
Historical Impact Analysis
Dependency Drift Detection
Automated Architecture Documentation
Advanced Incident Correlation
Impact Prediction
```

---

# 42. Resume Positioning

### Project Title

**Distributed Microservice Dependency & Impact Analysis Platform**

### Short Description

> Built a Java/Spring Boot distributed-systems platform that discovers REST, Kafka, database, and runtime dependencies, constructs a live dependency graph, and performs graph-based change-impact and failure blast-radius analysis.

### Technologies

```text
Java 21
Spring Boot
Microservices
Kafka
PostgreSQL
Redis
OpenTelemetry
React
TypeScript
Docker
Kubernetes
JUnit
Testcontainers
AWS
GitHub Actions
```

---

# 43. Resume Highlights

Potential resume bullets:

- Built a distributed dependency intelligence platform using **Java 21, Spring Boot, Kafka, PostgreSQL and Redis** to discover and model REST, Kafka, database and runtime service relationships.

- Implemented a **bounded graph-traversal impact engine** to identify direct/indirect downstream consumers, dependency paths, affected APIs, Kafka topics and owning teams for service changes.

- Developed event-driven dependency ingestion using **Kafka with idempotent consumers, retry/DLQ handling and Redis caching**, while integrating OpenTelemetry for runtime dependency correlation.

- Implemented **JWT/RBAC security, JUnit/Mockito unit testing, Testcontainers integration testing, Docker-based deployment and GitHub Actions CI/CD**.

---

# 44. Project Philosophy

The project follows four principles:

### 1. Deterministic Before Intelligent

Core impact calculations should be explainable before introducing AI.

### 2. Runtime + Static

Neither static architecture nor runtime telemetry alone provides complete dependency visibility.

### 3. Bounded Complexity

Graph traversal and asynchronous processing must have explicit limits.

### 4. Production-Oriented Engineering

The project demonstrates:

```text
Security
Reliability
Testing
Observability
Scalability
Maintainability
```

rather than only feature development.

---

# 45. Final Architecture Principle

> **Keep dependency collection asynchronous, dependency storage reliable, graph traversal bounded, impact analysis deterministic and explainable, and runtime behavior observable.**

---

# 46. Status

```text
Project Status: In Development

Architecture: Defined
PRD: Defined
Database Design: Defined
API Specification: Defined
UI/UX Specification: Defined
Technical Specification: Defined

Implementation: In Progress
```

---

# 47. License

Add the project's chosen license here.

Example:

```text
MIT License
```

---

# 48. Author

**Arman Ali**

Full Stack / Java Developer

```text
Java • Spring Boot • Microservices • Kafka
React • Node.js • PostgreSQL • Redis
Docker • AWS • CI/CD
```

---

## ⭐ Core Project Statement

> **Before I change a service, tell me what can break because of that change.**
>
> **If a service is already failing, tell me what other services and user journeys may be affected.**
