# Anti-Fraud-System

REST API project (for learning purposes) a'la banking system in hexagonal architecture. 

## Tech stack:
- Spring Boot 3.2.4
- Spring Validation
- Spring Security 6
- Spring Data JPA
- Lombok
- PostgreSQL
- Docker
- Spock

## Getting started

### Instalation
1. **Clone repository**
```shell
git clone https://github.com/bgnatowski/anti-fraud-system
cd anti-fraud-system
```
2. **Run Postgres on docker**
```shell
docker-compose up -d
```
3. **Build project**
```shell
mvn clean install
```
4. **Run the application**
```shell
mvn spring-boot:run
```

5. **Testing with Postman**
    - Import the provided Postman collection (`anti-fraud-system.postman_collection.json`).
    - Ensure that the server is running on `localhost:1102`.
    - Execute the requests directly from Postman.

## Notes

- All transactions are validated based on specific rules such as amount thresholds and IP correlations.
- Ensure that the necessary environment variables and database configurations are set before running the server.

## #TODO
* better swagger documentation
* connect transaction logic with user-account-card for better validation
* create utility class for endpoints urls