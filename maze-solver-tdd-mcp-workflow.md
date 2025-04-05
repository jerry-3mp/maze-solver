# TDD Development with Model Context Protocol (MCP) Version 11

## Core Rules
1. **MOST IMPORTANT RULE**: Only make file changes using MCP when "Proceed MCP" is explicitly input
2. **IMPORTANT**: Do not discuss or suggest maze-solving algorithms or implementations - these must be developed by the user only
3. Code blocks in responses are for discussion and preview only, not for file changes
4. Tests and implementation must be presented in separate responses
5. Only show code snippets when explicitly requested with "Show Actions Code"
6. "Read file" commands don't require waiting for "Proceed MCP" - immediately read files when needed

## Instructions
For each feature or task:

1. First, analyze requirements and create test cases
2. Use Model Context Protocol (MCP) to plan file additions or modifications
3. Present test actions and implementation actions in separate responses
   - First response should only contain test file actions
   - After "Proceed MCP" for tests, present implementation actions in a new response
4. Each MCP plan should start with a brief summary of changes
5. Be precise about file paths - use complete paths that match the project structure
6. Discussion commands:
   - Type "Discussion Time" when you want to discuss design patterns or other concerns
   - Type "Plan Actions" when you're ready to receive the MCP action plan
   - Type "Improve Actions" when you want to improve the last received MCP action plan
   - Type "Show Actions Code" when you want to see code snippets for planned actions
   - If no "Plan Actions" is input, provide content using code blocks in response only
7. After each "Proceed MCP" implementation, suggest a commit message following Conventional Commits 1.0.0 format
   - Format: `<type>[optional scope]: <description>`
   - Keep messages under 72 characters
   - Be concise but descriptive (e.g., `feat: implement find methods for maze solver service`)

## Response Style
1. Be concise - avoid repetition or unnecessary explanations for an experienced backend engineer
2. Keep responses brief to save traffic due to usage limits
3. Eliminate non-essential details or repetition
4. Focus on technical content rather than explanations of basic concepts
5. When showing file paths, format them as inline code (using backticks) for better readability

## TDD Implementation Process
Follow these phases strictly:

1. **Red Phase**:
   - Write test cases that define the expected behavior
   - Add method stubs with `UnsupportedOperationException` in implementation classes
   - Tests should test the expected final behavior, not the temporary exceptions
   - Include different scenarios (basic operations, error conditions)

2. **Green Phase**:
   - Implement the methods with minimal code to make tests pass
   - Focus on correctness, not optimization at this stage

3. **Refactor Phase** (if needed):
   - Clean up the implementation while ensuring tests continue to pass
   - Ensure code follows project patterns and conventions

## Project Conventions
1. Respect existing code style and naming patterns
2. Be attentive to naming conventions in the existing codebase - choose names that follow established patterns and clearly communicate purpose
3. When updating an interface, ensure all implementations are updated accordingly
4. Ensure method signatures are consistent across interfaces and implementations
5. Follow the project's existing style for documentation in interfaces
6. Be mindful of backwards compatibility requirements when modifying existing methods
7. When making repository assumptions (like custom query methods), verify they exist or note their requirement
8. Always check method documentation when implementing or updating logic
9. Update documentation if it doesn't match the refined behavior
10. When improving actions, first read existing files thoroughly to understand structure
11. Make minimal changes to achieve the required functionality - focus on changes that directly address the requirements
12. Follow existing design patterns and code organization principles in the project
13. Improvement scope includes both implementation and documentation
14. When presenting implementation options, consider the experience level of the developer with the programming language

## Maven Configuration Best Practices
1. **Dependency Organization**:
   - Group dependencies by purpose (core, web, database, testing, etc.)
   - Use XML comments to clearly separate dependency categories
   - Maintain alphabetical ordering within each category when possible

2. **Version Management**:
   - Define all version numbers as properties in the `<properties>` section
   - Use the format `<[group-id].[artifact-id].version>value</[group-id].[artifact-id].version>` for consistency
   - Include comments for significant version constraints or dependencies

3. **Plugin Configuration**:
   - Configure plugins with appropriate execution phases
   - Document plugin purposes with XML comments
   - Use the maven-enforcer-plugin to ensure dependency convergence
   - Configure the maven-compiler-plugin to match the project's Java version
   - Set appropriate plugin versions to avoid using default versions

4. **Profiles**:
   - Define environment-specific profiles (dev, test, prod)
   - Use profiles for conditional dependency inclusion
   - Configure environment-specific plugin behavior in relevant profiles
   - Document profile purposes and activation methods

5. **Repository Management**:
   - Define repositories only when necessary (prefer central Maven repository)
   - Include repository IDs, names, and URLs when custom repositories are required
   - Configure mirror settings appropriately when using artifact repositories

6. **Build Optimization**:
   - Set appropriate `<scope>` for dependencies to minimize runtime classpath
   - Configure the maven-surefire-plugin for efficient test execution
   - Use the maven-failsafe-plugin for integration tests
   - Enable parallel test execution when appropriate

## API Development Practices
1. Use type-safe returns in controllers rather than relying solely on annotations for Swagger documentation
2. Follow enterprise best practices for API development including:
   - Proper error responses with consistent structure
   - Consistent data formats across endpoints
   - Appropriate HTTP status codes
   - Clear validation error messages
3. Use `Instant` instead of `LocalDateTime` for timestamps in API responses to ensure consistent UTC representation regardless of server or client timezone
4. Include proper pagination, sorting and filtering for collection endpoints
5. Implement appropriate caching headers where applicable

## README.md File Guidelines
1. Keep the README.md file concise and scannable
2. Focus on essential information that developers need to know
3. Use bullet points rather than lengthy paragraphs
4. For the development journal, use numbered steps instead of dated entries for short-term projects
5. Only include technologies actually used in the project
6. Prioritize information that helps new team members get started quickly

## Interaction Commands
You can use these commands at any point in the process:
- "Discussion Time" - To discuss design patterns, architecture, or any concerns you have
- "Plan Actions" - To return to the action planning phase
- "Improve Actions" - To improve the last received MCP action plan
- "Show Actions Code" - To see code snippets for planned actions
- "Proceed MCP" - To approve and implement the proposed MCP actions
- "Summary New Instruction" - To get a summary of all hints, preferences, and guidance provided during the conversation to improve future interactions (not a summary of completed tasks)

## Additional Command Patterns

In addition to the core interaction commands, the following specialized command patterns can be used to handle specific situations:

### "Provide Code Only"
When file changes are too large or complex for "Proceed MCP" editing, use this command to request code snippets without directly modifying files.

**Usage:** Type "Provide Code Only" followed by specific instructions about what code is needed.

**Behavior:** 
- The assistant will analyze relevant files first
- The assistant will provide only the necessary code blocks for the requested changes
- Code will be presented in markdown code blocks with appropriate language syntax highlighting
- No file system modifications will be made
- Ideal for previewing large changes before implementation or when dealing with complex file structures

**Example:**
```
"Provide Code Only" to show the implementation for a user authentication service
```

## Example Format

### First Response - Test Actions
```
## MCP Action Plan Summary - Tests
Brief overview of what test changes will be made and why.

## Test Case MCP Actions
- Create file: `/Users/jerry3mp/Workspace/maze-solver/maze-solver-server/src/test/java/[path/to/test_file.java]`
- Modify file: `/Users/jerry3mp/Workspace/maze-solver/maze-solver-server/src/test/java/[path/to/existing_test.java]`
```

### After "Proceed MCP" - Implementation Actions
```
## MCP Action Plan Summary - Implementation
Brief overview of what implementation changes will be made and why.

## Implementation MCP Actions
- Create file: `/Users/jerry3mp/Workspace/maze-solver/maze-solver-server/src/main/java/[path/to/implementation.java]`
- Modify file: `/Users/jerry3mp/Workspace/maze-solver/maze-solver-server/src/main/java/[path/to/existing_implementation.java]`
```

## Current Project Context
- **Target Programming Language:** Java 21 (JDK 21)
- **Project Directory:** /Users/jerry3mp/Workspace/maze-solver
- **Frameworks/Libraries:** 
  - Spring Boot 3.4.4
  - Flyway (database migration)
  - PostgreSQL
  - JUnit 5 (testing)
- **Documentation:**
  - PlantUML (.puml) for diagrams
  - README.md for project documentation
- **Feature Description:** Implement the following functionality:
  1. Generate random maze and its solution

## Database Tables

## Project File Structure
```
# Paste the output of `find . -type f | sort` or `tree ./` command here to provide
# a comprehensive view of existing project files and structure.
# This will help with understanding the project organization and making precise file path references.

./
├── LICENSE
├── README.md
├── docker-compose.pg.volume.yml
├── docker-compose.yml
├── maze-solver-server
│   ├── HELP.md
│   ├── events
│   │   └── event.json
│   ├── mvnw
│   ├── mvnw.cmd
│   ├── pom.xml
│   ├── sam.md
│   ├── samconfig.toml
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── io
│   │   │   │       └── jistud
│   │   │   │           └── mazesolver
│   │   │   │               └── server
│   │   │   │                   ├── MazeSolverServerApplication.java
│   │   │   │                   ├── StreamLambdaHandler.java
│   │   │   │                   ├── config
│   │   │   │                   │   └── SwaggerConfig.java
│   │   │   │                   └── controller
│   │   │   │                       └── StatusController.java
│   │   │   └── resources
│   │   │       ├── application-lambda.properties
│   │   │       ├── application-local.properties
│   │   │       ├── application-test.properties
│   │   │       ├── application.properties
│   │   │       ├── db
│   │   │       │   └── migration
│   │   │       │       └── common
│   │   │       ├── static
│   │   │       └── templates
│   │   └── test
│   │       └── java
│   │           └── io
│   │               └── jistud
│   │                   └── mazesolver
│   │                       └── server
│   │                           └── MazeSolverServerApplicationTests.java
│   └── template.yaml
└── maze-solver-tdd-mcp-workflow.md
```