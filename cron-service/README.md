# Cron Service

Scheduler service that runs periodic jobs, stores generated reports, and archives old audit logs.

## Jobs

- Every minute: count total users, orders, and products
- Every midnight: generate daily sales report
- Every Sunday: archive old audit logs

## Run

```powershell
.\gradlew.bat -p cron-service bootRun
```

## Ports

- Application: `7777`
- PostgreSQL: `5435`
