# Flight App

Full-stack flight management project with a React frontend, Spring Boot backend, and MySQL persistence.

## Backend

The backend exposes REST APIs at `http://localhost:8080/api/flights`.
When the React app runs in Docker, nginx proxies `/api/*` to the backend container, so the UI calls `/api/flights` from `http://localhost:5173`.

Endpoints:

- `POST /api/flights` - save a flight
- `GET /api/flights` - list flights
- `GET /api/flights/{code}` - find by code
- `GET /api/flights/carrier/{carrier}` - find by carrier
- `GET /api/flights/route?source=...&destination=...` - find by route
- `GET /api/flights/price?min=...&max=...` - find by price range
- `DELETE /api/flights/{code}` - delete by code

## MySQL Configuration

Default backend connection:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/flightdb?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=flightuser
spring.datasource.password=NewPassword123
```

Create the local MySQL user/database manually if you are not using Docker:

```sql
CREATE DATABASE IF NOT EXISTS flightdb;
CREATE USER IF NOT EXISTS 'flightuser'@'%' IDENTIFIED BY 'NewPassword123';
ALTER USER 'flightuser'@'%' IDENTIFIED BY 'NewPassword123';
GRANT ALL PRIVILEGES ON flightdb.* TO 'flightuser'@'%';
FLUSH PRIVILEGES;
```

## Run With Docker

Start Docker Desktop first, then run:

```bash
docker compose up --build
```

Services:

- React app: `http://localhost:5173`
- Backend API through frontend nginx: `http://localhost:5173/api/flights`
- Backend API from Docker network: `http://flight-service:8080/api/flights`
- MySQL from backend container: `mysql:3306`

## Run Locally

Start MySQL first, then run the backend:

```bash
/tmp/apache-maven-3.9.9/bin/mvn spring-boot:run
```

Run the frontend:

```bash
cd flight-app
npm install
npm run dev
```

## Tests

Tests use H2 in MySQL compatibility mode, so they do not require a running MySQL server:

```bash
/tmp/apache-maven-3.9.9/bin/mvn test
```
