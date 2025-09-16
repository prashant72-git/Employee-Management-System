# EMS Backend (Spring Boot + Flyway + MySQL)

Modules: Employee, Attendance, Payroll

## Quick start
1. Create database:
```sql
CREATE DATABASE ems_dev CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
2. Update `src/main/resources/application.yml` datasource credentials.
3. Run app:
```bash
./mvnw spring-boot:run
```
4. Swagger UI: `http://localhost:8080/swagger-ui/index.html`

## Useful endpoints
- Employees
  - `GET /api/employees`
  - `POST /api/employees`
  - `GET /api/employees/{id}`
  - `PUT /api/employees/{id}`
  - `DELETE /api/employees/{id}`
- Attendance
  - `POST /api/attendance/check-in` body: `{ "employeeId": 1, "timestampUtc": "2025-08-28T03:15:00Z" }`
  - `POST /api/attendance/check-out` body: `{ "employeeId": 1, "timestampUtc": "2025-08-28T11:30:00Z" }`
- Payroll
  - `POST /api/payroll/runs/preview?month=2025-08`
  - `POST /api/payroll/runs/{month}/lock`  e.g. `/api/payroll/runs/2025-08/lock`


