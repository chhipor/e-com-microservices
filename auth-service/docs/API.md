# Auth Service API

## Authentication

### `POST /auth/register`

Creates a new user with the default `USER` role.

Request body:

```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "password": "Strong@123"
}
```

### `POST /auth/login`

Returns access and refresh tokens.

Request body:

```json
{
  "email": "john@example.com",
  "password": "Strong@123"
}
```

### `POST /auth/refresh`

Returns a new access token and refresh token.

Request body:

```json
{
  "refreshToken": "..."
}
```

## Users

### `GET /users`

Requires `USER_READ`.

### `GET /users/{id}`

Requires `USER_READ`.

### `PUT /users/{id}`

Requires `USER_UPDATE`.

### `DELETE /users/{id}`

Requires `USER_DELETE`.
