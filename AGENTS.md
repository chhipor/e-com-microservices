# iShop Agent Guide

## Project overview

iShop is a Spring Boot microservices application for an electronics store.

- `gateway-service`: API gateway, routing, browser-facing CORS, port `8080`
- `auth-service`: authentication and users, port `6666`
- `operation-service`: products, customers, and orders, port `8888`
- `reporting-service`: reports, port `9999`
- `cron-service`: scheduled jobs, port `7777`
- `docker/postgres/`: PostgreSQL initialization scripts
- `postman/ishop_api_collection.json`: Insomnia-compatible API collection

The frontend runs at `http://localhost:3001` or `http://127.0.0.1:3001` and should call the gateway at `http://localhost:8080`.

## Backend conventions

- Keep browser-facing CORS configuration in `gateway-service` only.
- Do not add downstream CORS filters that can duplicate gateway response headers.
- Gateway CORS must explicitly allow the frontend origins; do not use wildcard origins with credentials.
- Public product reads are:
  - `GET /api/operations/products`
  - `GET /api/operations/products/{id}`
- Product writes and administrative endpoints remain protected.
- Preserve the existing JWT authentication and refresh-token behavior.
- Use the existing controllers, DTOs, and OpenAPI definitions. Do not invent endpoints or response fields.
- Product responses contain `id`, `name`, `sku`, `description`, `unitPrice`, `quantityInStock`, `active`, `createdAt`, and `updatedAt`.

## Database notes

- PostgreSQL service ports are `5555` auth, `5433` operation, `5434` reporting, and `5435` cron.
- Auth schema files are in `auth-service/src/main/resources/schema.sql` and `data.sql`.
- Existing Docker volumes do not rerun initialization scripts. Auth local development uses Hibernate `ddl-auto: update` to recover missing mapped tables.
- Never delete database volumes without explicit user approval.

## Frontend guidance

- Use a typed centralized API client.
- Public home, products, and product-detail pages must not require login.
- Public product requests should not require an Authorization header.
- Match TypeScript types to actual backend DTOs, including UUID IDs and timestamps.
- Keep real API data; do not silently replace failures with mock data.
- Handle loading, empty, error, 401, and 403 states explicitly.

## Validation and commands

Run the relevant Gradle task when the wrapper is available:

```powershell
.\gradlew.bat test
```

For a service-specific build or run:

```powershell
.\gradlew.bat :operation-service:test
.\gradlew.bat :operation-service:bootRun
```

Docker commands require Docker Desktop to be running:

```powershell
docker compose up -d --build
docker compose ps
```

When testing CORS, use the exact browser origin and verify that the gateway response contains only one `Access-Control-Allow-Origin` and one `Access-Control-Allow-Credentials` header.

## Change discipline

- Inspect existing changes before editing; preserve unrelated user work.
- Prefer small, targeted patches.
- Use `apply_patch` for file edits.
- Do not run destructive Git or Docker volume commands without explicit approval.

