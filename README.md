# Maze Solver

A maze generation and solution application.

## Project Tech Stack

- Java 21 (JDK 21)
- Spring Boot 3.4.4
- PostgreSQL
- Flyway (database migration)
- JUnit 5 (testing)
- Maven (build tool)

## Project Knowledge

### API Endpoints

- `GET /api/status`: Returns a simple text message confirming the server is running

*More endpoints will be added as the project develops*

## Development Steps

1. Initialize project structure with Spring Boot
2. Configure TDD development workflow using MCP Version 10
3. Set up README.md documentation
4. Implement basic status controller with CORS support
5. Survey AWS RDS PostgreSQL versions and add related configurations
6. Configure Spring Boot for AWS Lambda deployment
   - https://www.youtube.com/watch?v=HQQD4dndDpE
   - https://github.com/aws/serverless-java-container/wiki/Quick-start---Spring-Boot3
7. Set up AWS RDS database and Lambda connectivity
8. Implement secure credential management with GitHub Secrets
