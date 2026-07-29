# Operation Service

Product, customer, and order management service.

## Run

```powershell
.\gradlew.bat -p operation-service bootRun
```

## Ports

- Application: `8888`
- PostgreSQL: `5433`

## JWT

This service validates JWT access tokens signed with the same shared secret as `auth-service`.

## API

- `POST /products`
- `GET /products`
- `GET /products/{id}`
- `PUT /products/{id}`
- `DELETE /products/{id}`
- `POST /customers`
- `GET /customers`
- `GET /customers/{id}`
- `PUT /customers/{id}`
- `DELETE /customers/{id}`
- `POST /orders`
- `GET /orders`
- `GET /orders/{id}`
- `PUT /orders/{id}`
- `DELETE /orders/{id}`
