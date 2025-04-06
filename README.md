# Maze Solver

A maze generation and solution application.

## Project Tech Stack

- Java 21 (JDK 21)
- Spring Boot 3.4.4
- PostgreSQL
- Flyway (database migration)
- JUnit 5 (testing)
- Maven (build tool)
- AWS Lambda
- AWS RDS
- React 19
- TypeScript
- Vite
- AWS S3

## Project Knowledge

### API Endpoints

- `GET /api/status`: Returns a simple text message confirming the server is running
- `POST /api/v1/mazes`: Generates a random maze with specified dimensions
- `GET /api/v1/mazes`: Returns a paginated list of maze summaries
- `GET /api/v1/mazes/{id}`: Returns detailed information about a specific maze
- `PUT /api/v1/mazes/{id}/solve`: Solves the specified maze and returns the solution

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
9. Add rule against discussing maze-solving algorithms before develop maze-related feature
10. Implement Maze and MazeBuilder using the staged builder pattern
11. Implement solve method with recursive DFS
12. Implement API for generating, retrieving, and solving mazes
13. Initialize Vite + React + TypeScript frontend client folder
14. Survey and set up S3 static website deploy by GitHub workflow
    - https://www.youtube.com/watch?v=tajK4ezyuNc
15. Test client to lambda connection by StatusComponent
16. Implement header app bar and drawer
17. Implement maze visualization
18. Refactor state management with Context/Provider pattern
    - https://dev.to/kurmivivek295/contextprovider-pattern-4m1c
