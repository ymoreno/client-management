# 🏦 Client Management Backend - Technical Test
This project is a Java (Spring Boot) backend application implementing a hexagonal architecture for managing clients, financial products, and transactions.

## 📐 Architecture
The project follows a hexagonal architecture with separation of concerns:

**Domain**: Entities, business rules, use cases.
**Ports**: Interfaces defining inbound/outbound communication.
**Adapters**: Concrete implementations (REST, JPA, logging, security).
**Infrastructure**: Database, security, and logging configurations.

## Prerequisites

Java 17+
Maven 3.8+
Docker & Docker Compose

## Database Setup with Docker
The project uses PostgreSQL. Run:

`docker-compose up -d
`

docker-compose.yml:
```yaml
version: '3.8'
services:
  postgres:
    image: postgres:13
    container_name: client_management_db
    environment:
      POSTGRES_DB: test
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: admin
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

volumes:
  postgres_data:

```

## Application Configuration
In src/main/resources/application.yml:
   ```yaml
spring:
   datasource:
   	url: jdbc:postgresql://localhost:5432/test
   	username: postgres
   	password: admin
   jpa:
   	hibernate:
   		ddl-auto: update
   	show-sql: true
   	properties:
   		hibernate:
   			format_sql: true
```

## Run the Application
Es importante recordar que se deben crear manualmente el siguiente script:

### Conectarse a PostgreSQL
```bash
psql -h localhost -U postgres -d test
```
### Ejecutar el script
```bash
\i database-setup.sql
```
Ahora si podemos ejecutar la aplicación.
```bash
   mvn spring-boot:run
```
## Exposed Endpoints

Clients: /client
Products: /product
Transactions: /transaction

## Run Tests
El proyecto incluye JUnit 5 para pruebas unitarias en servicios y controladores:
```bash
   mvn test
```
Ademas tiene pruebas de integracion que se pueden ejecutar desde raiz
```bash
   quick-test.sh
   smoke-test-curl.sh
```

## 📌 Notes

**Security**: Configured with Spring Security (Basic Auth).
**Logging**: Managed via LogPort → LogAdapter using SLF4J/Logback.
**Validations**: Handled in the domain layer with support from Bean Validation (jakarta.validation).
**Hexagonal Architecture**: Domain is isolated from frameworks and infrastructure.


## 🔑 Authentication
The API uses Basic Auth:

Admin: admin / admin
User: user / password

Permissions vary by role.
