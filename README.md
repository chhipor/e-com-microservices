# Miterm

Midterm project with four Spring Boot services and separate PostgreSQL databases:

- `auth-service` for registration, login, refresh tokens, and user management
- `operation-service` for products, customers, and orders
- `reporting-service` for read-only reporting and snapshot storage
- `cron-service` for scheduled jobs and job status

## Prerequisites

- Java 21
- Docker Desktop
- Gradle wrapper included in the repo

## Quick Start

1. Start the databases:

```powershell
docker compose up -d
```

2. Run each service from the repository root in its own terminal:

```powershell
.\gradlew.bat -p auth-service bootRun
.\gradlew.bat -p operation-service bootRun
.\gradlew.bat -p reporting-service bootRun
.\gradlew.bat -p cron-service bootRun
```

## Local Ports

Application ports:

- Auth service: `http://localhost:6666`
- Operation service: `http://localhost:8888`
- Reporting service: `http://localhost:9999`
- Cron service: `http://localhost:7777`

Database ports:

- Auth PostgreSQL: `5432`
- Operation PostgreSQL: `5433`
- Reporting PostgreSQL: `5434`
- Cron PostgreSQL: `5435`

## Postman

Import this collection to exercise all endpoints:

- [postman/miterm-full.postman_collection.json](postman/miterm-full.postman_collection.json)

It includes variables for:

- `authBaseUrl`
- `operationBaseUrl`
- `reportingBaseUrl`
- `cronBaseUrl`
- `accessToken`
- `refreshToken`
- created entity IDs for chaining requests

## Services

### Auth Service

- Register, login, refresh token
- User CRUD
- Default admin:
  - Email: `admin@local.test`
  - Password: `Admin@1234!`

### Operation Service

- Product CRUD
- Customer CRUD
- Order CRUD

### Reporting Service

- Product, order, customer, and sales reports
- Store snapshot endpoints for each report type

### Cron Service

- Job status endpoint
- Scheduled report and archive jobs

## Notes

- The root `docker-compose.yml` contains all PostgreSQL services.
- Each Spring Boot service is intentionally run separately.
- The services use the shared JWT secret defined in their `application.yml` files.
