# AGENTS.md

Guidelines, architecture conventions, and development rules for AI agents and human developers in `price-service`.

---

## 1. Project Overview

`price-service` is a Spring Boot (Java 25) microservice that provides the applicable final price of a product for a brand at a given date/time based on priority and recency rules.

---

## 2. Project Architecture (Onion / Hexagonal)

The project strictly follows **Onion / Hexagonal Architecture** with inward-pointing dependencies:

- **Domain Core (`domain`)**: Pure Java (records, interfaces, exceptions). **Zero** external framework dependencies (no Spring, Jakarta, Jackson, etc.).
- **Application Layer (`application`)**: Use case implementations and inbound ports. No Spring component annotations (`@Service`/`@Component`); wired explicitly as `@Bean`s in `AppConfiguration`.
- **Infrastructure Layer (`infrastructure`)**: Adapters (REST controllers, JDBC repository via `JdbcClient`, MapStruct mappers, configuration, exception handlers).
- **Contract-First API**: REST contract defined in `src/main/resources/openapi.yaml`. Generated classes in `build/generated` must not be modified manually.

---

## 3. Project Structure

```
price-service/
├── build.gradle
├── settings.gradle
├── docker/
│   └── Dockerfile
├── src/
│   ├── main/
│   │   ├── java/io/github/agomezlucena/priceservice/
│   │   │   ├── domain/                              # DOMAIN CORE (Pure Java)
│   │   │   │   ├── PriceInfo.java                   # Domain entity (record)
│   │   │   │   ├── PriceRepository.java             # Outbound port interface
│   │   │   │   ├── PriceNotFoundException.java      # Domain exception
│   │   │   │   └── criteria/                        # Domain criteria query specifications
│   │   │   ├── application/                         # APPLICATION LAYER
│   │   │   │   ├── PriceInfoFinder.java             # Inbound port interface
│   │   │   │   ├── FindPriceInfoUseCase.java        # Use case orchestrator
│   │   │   │   ├── PriceInfoApplicationDateQuery.java # Query DTO
│   │   │   │   └── PriceInfoResponse.java           # Response DTO
│   │   │   ├── infrastructure/                      # INFRASTRUCTURE ADAPTERS
│   │   │   │   ├── AppConfiguration.java            # Spring Bean definitions
│   │   │   │   ├── PriceRestController.java         # REST Controller (Inbound adapter)
│   │   │   │   ├── PriceControllerAdvice.java       # Global RFC 9457 exception handler
│   │   │   │   ├── PriceInfoResponseMapper.java     # MapStruct mapper
│   │   │   │   ├── PriceSqlRepository.java          # Spring JdbcClient adapter (Outbound)
│   │   │   │   └── criteria/                        # SQL criteria translators
│   │   │   └── PriceServiceApplication.java         # Spring Boot entry point
│   │   └── resources/
│   │       ├── application.properties               # App configuration
│   │       ├── openapi.yaml                         # OpenAPI 3.1.0 contract
│   │       └── db/changelog/                        # Liquibase migrations
│   └── test/
│       └── java/io/github/agomezlucena/priceservice/
│           ├── PriceServiceApplicationTests.java
│           ├── application/                         # Unit tests (Use cases)
│           ├── domain/                              # Unit tests (Domain / Criteria)
│           └── infrastructure/                      # Integration & Adapter tests
```

---

## 4. How to Run & Test

Execute commands from the project root using the Gradle wrapper:

```bash
# Code generation & Compilation
./gradlew openApiGenerate
./gradlew compileJava
./gradlew testClasses

# Run all tests
./gradlew test

# Run a specific test class
./gradlew test --tests "io.github.agomezlucena.priceservice.infrastructure.PricesRestControllerItTest"

# Run the application locally (port 8080)
./gradlew bootRun

# Build Docker image
./gradlew buildDockerImage
# or: docker build -t price-service:latest -f docker/Dockerfile .
```

---

## 5. Agent Development Rules & Workflow

When modifying code or implementing new features, agents must strictly follow:

### 1. Respect Architecture & Layer Boundaries
- Keep `domain` 100% pure Java. Do not add Spring, Jakarta, or Jackson annotations.
- Do not annotate application use cases with `@Service` or `@Component`; register them in `AppConfiguration.java`.
- Follow contract-first API design: update `openapi.yaml` first, then regenerate code and update adapters/mappers.
- Follow RFC 9457 `ProblemDetail` for all error responses handled in `PriceControllerAdvice`.

### 2. Test-Driven Development (TDD)
- **Always write or update tests first** before writing the implementation (Red-Green-Refactor cycle).
- Ensure new tests fail for the intended reason before adding code to make them pass.

### 3. Code Reuse & Avoid Duplication (DRY)
- Avoid repeating code or logic across layers.
- Reuse existing domain criteria, query models, translators, DTOs, and utility methods whenever possible.

### 4. Unit Testing & Mockito Usage
- Use `@ExtendWith(MockitoExtension.class)` for unit tests on classes that have non-data dependencies (e.g., use cases depending on repositories or service interfaces).
- **Do not mock pure data objects/records** (such as domain entities, DTOs, or query criteria)—instantiate them directly.
- Use `*Test.java` for unit tests and `*ItTest.java` for integration tests (Spring context, DB, or MockMvc).

---

## 6. Agent Verification Checklist

Before finalizing any task, verify:
- [ ] Tests written first following TDD?
- [ ] Unit tests with non-data dependencies use Mockito (`@ExtendWith(MockitoExtension.class)`)?
- [ ] No code duplication; existing components reused where possible?
- [ ] Architecture invariants maintained (pure domain, beans configured in `AppConfiguration`)?
- [ ] Code compiles cleanly (`./gradlew compileJava`)?
- [ ] All tests pass (`./gradlew test`)?
