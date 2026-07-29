# Auth Service

Authentication and user management service for the midterm project.

## Stack

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- JWT
- PostgreSQL
- Lombok
- Validation

## Run

1. Start the databases from the root `docker-compose.yml`.
2. Run the service from the repository root:

```powershell
.\gradlew.bat -p auth-service bootRun
```

## Ports

- Application: `6666`
- PostgreSQL: `5432`

## Default Admin

- Email: `admin@local.test`
- Password: `Admin@1234!`

## API

Public endpoints:

- `POST /auth/register`
- `POST /auth/login`
- `POST /auth/refresh`

Protected endpoints:

- `GET /users`
- `GET /users/{id}`
- `PUT /users/{id}`
- `DELETE /users/{id}`

## Notes

- Passwords are hashed with BCrypt.
- JWT access tokens are stateless.
- Refresh tokens are stored in PostgreSQL.
- Permission checks are enforced with `@PreAuthorize`.
