# Reporting Service

Read-only reporting service that generates summaries and stores report snapshots.

## Run

```powershell
.\gradlew.bat -p reporting-service bootRun
```

## Ports

- Application: `9999`
- PostgreSQL: `5434`

## API

Read endpoints:

- `GET /reports/products`
- `GET /reports/orders`
- `GET /reports/customers`
- `GET /reports/sales`

Snapshot endpoints:

- `POST /reports/products/store`
- `POST /reports/orders/store`
- `POST /reports/customers/store`
- `POST /reports/sales/store`
