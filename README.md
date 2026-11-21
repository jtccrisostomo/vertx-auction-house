
# Vert.x Auction House

[![CI](https://github.com/jc-crisostomo/vertx-auction-house/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/jc-crisostomo/vertx-auction-house/actions/workflows/ci.yml)

A reactive auction house backend built with Java 21, Vert.x,
and PostgreSQL, designed around asynchronous processing,
transactional consistency, and real-time auction events.

## Tech Stack

| Category | Technology |
|----------|------------|
| Language | Java 21 |
| Framework | Vert.x 5 |
| Database | PostgreSQL 17 |
| Database Migrations | Flyway |
| Build System | Gradle (Kotlin DSL) |
| Testing | JUnit 6, Testcontainers |
| Code Quality | Checkstyle, SpotBugs |
| Test Coverage | JaCoCo |
| Continuous Integration | GitHub Actions |
| Containerization | Docker, Docker Compose |

## Features

### Implemented

- Reactive HTTP server
- Application liveness and database readiness endpoints
- Environment-based database configuration
- Asynchronous PostgreSQL connection pooling
- Version-controlled database migrations
- Isolated database integration testing
- Automated code quality and static analysis
- Continuous integration and test coverage reporting
- Version-controlled Git hooks

### Planned

- User authentication and authorization
- User and organization management
- Auction creation and lifecycle management
- Bid placement and retrieval
- Auction clearing with price-time priority
- Partial bid allocation
- Real-time auction notifications via WebSocket
- OpenAPI documentation
- Performance and concurrency testing

## Getting Started

### Prerequisites

- JDK 21
- Docker with Docker Compose
- Git

Gradle is provided through the Gradle Wrapper.
No global Gradle installation is required.

On Windows, use `.\gradlew.bat` instead of `./gradlew`
for all Gradle commands.

### Clone the Repository

```bash
git clone https://github.com/jc-crisostomo/vertx-auction-house.git
cd vertx-auction-house
```

### Database Setup

The project uses PostgreSQL 17 with Docker Compose.

Create a local environment configuration from `.env.example`.

On Windows:

```powershell
Copy-Item .env.example .env
```

On Linux or macOS:

```bash
cp .env.example .env
```

Replace the placeholder password in `.env` with a local
development password.

Start PostgreSQL:

```bash
docker compose up -d postgres
```

Verify the database container:

```bash
docker compose ps
```

Ensure the PostgreSQL container is healthy before proceeding.

To stop and remove the container while preserving database data:

```bash
docker compose down
```

Database credentials must not be committed to version control.

### Database Migrations

Database schema changes are managed using Flyway.

Migration files are located in:

`src/main/resources/db/migration/`

Apply pending migrations:

```bash
docker compose run --rm flyway migrate
```

Inspect migration status:

```bash
docker compose run --rm flyway info
```

Validate applied migrations:

```bash
docker compose run --rm flyway validate
```

Previously applied migrations must not be modified.
Create a new versioned migration for subsequent schema changes.

### Application Configuration

The application uses the following environment variables:

| Variable | Default | Description |
|----------|---------|-------------|
| `POSTGRES_HOST` | `127.0.0.1` | Database hostname |
| `POSTGRES_PORT` | `5432` | Database port |
| `POSTGRES_DB` | Required | Database name |
| `POSTGRES_USER` | Required | Database username |
| `POSTGRES_PASSWORD` | Required | Database password |

Docker Compose reads `.env` automatically. The Java application
requires these variables to be set in its process environment.

Use the same database credentials configured in `.env`.

#### Windows (PowerShell)

```powershell
$env:POSTGRES_HOST = "127.0.0.1"
$env:POSTGRES_PORT = "5432"
$env:POSTGRES_DB = "auction_house"
$env:POSTGRES_USER = "auction_user"

$env:POSTGRES_PASSWORD = [System.Net.NetworkCredential]::new(
    '',
    (Read-Host "Database password" -AsSecureString)
).Password
```

#### Linux or macOS (Bash)

```bash
export POSTGRES_HOST=127.0.0.1
export POSTGRES_PORT=5432
export POSTGRES_DB=auction_house
export POSTGRES_USER=auction_user

read -rs -p "Database password: " POSTGRES_PASSWORD
echo
export POSTGRES_PASSWORD
```

Never hardcode database credentials in the application source code.

### Build

Ensure Docker is running before executing the build,
as integration tests require Testcontainers.

```bash
./gradlew clean build
```

The build includes compilation, automated tests,
Checkstyle, and SpotBugs verification.

### Run

Ensure PostgreSQL is running, database migrations have been applied,
and the required environment variables are configured.

```bash
./gradlew run
```

The application is available at:

`http://localhost:8080`

Verify the application:

```bash
curl http://localhost:8080/health
curl http://localhost:8080/ready
```

Both endpoints should return HTTP 200 when the application
and database are available.

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/health` | Application liveness check |
| GET | `/ready` | Database connectivity and readiness check |

### Liveness Check

`GET /health`

Returns HTTP 200 when the application is running,
regardless of database availability.

```json
{
  "status": "UP"
}
```

### Readiness Check

`GET /ready`

Returns HTTP 200 when the database is accessible.

```json
{
  "status": "UP"
}
```

Returns HTTP 503 when the database is unavailable.

```json
{
  "status": "DOWN"
}
```

## Testing

### Run Tests

Ensure Docker is running before executing the test suite.

```bash
./gradlew test
```

The HTML test report is available at:

`build/reports/tests/test/index.html`

### Database Integration Tests

Integration tests use Testcontainers to provision isolated
PostgreSQL 17 instances and apply Flyway migrations automatically.

Run database integration tests only:

```bash
./gradlew test --tests "*DatabaseIntegrationTest"
```

The tests do not require:

- A running Docker Compose database
- Local database credentials
- Manually configured database schemas

Temporary test containers are managed and cleaned up automatically.

### Test Coverage

Generate the JaCoCo coverage report:

```bash
./gradlew jacocoTestReport
```

The HTML coverage report is available at:

`build/reports/jacoco/test/html/index.html`

Coverage and verification reports are also available as
artifacts in GitHub Actions workflow runs.

## Code Quality

### Checkstyle

Checkstyle enforces consistent Java coding standards,
naming conventions, and source code formatting.

```bash
./gradlew checkstyleMain checkstyleTest
```

### SpotBugs

SpotBugs performs static bytecode analysis to identify
potential programming defects.

```bash
./gradlew spotbugsMain spotbugsTest
```

### Full Verification

Run all configured tests and quality checks:

```bash
./gradlew check
```

Generated verification reports are located under:

`build/reports/`

## Development Workflow

### Git Hooks

Enable the repository's version-controlled Git hooks after cloning:

```bash
git config --local core.hooksPath .githooks
```

The following checks run automatically:

| Hook | Verification |
|------|--------------|
| `pre-commit` | Checkstyle |
| `commit-msg` | Conventional Commit format |
| `pre-push` | Build, tests, and static analysis |

Docker must be running when pushing changes because the
pre-push hook executes the build, including integration tests.

### Branch Naming

| Prefix | Purpose |
|--------|---------|
| `feat/` | New features |
| `fix/` | Bug fixes |
| `refactor/` | Code restructuring |
| `test/` | Test additions or improvements |
| `docs/` | Documentation changes |
| `chore/` | Maintenance and tooling |
| `ci/` | Continuous integration changes |

### Commit Convention

This project follows Conventional Commits.

Examples:

```
feat(auction): implement auction creation
fix(auth): correct password validation
test: add database integration tests
refactor(bid): simplify bid allocation
docs: update API documentation
chore: configure static analysis
ci: update build workflow
```

### Contributing

1. Create a feature branch from the latest `main`.
2. Implement changes and write appropriate tests.
3. Run local build and quality checks.
4. Commit changes using Conventional Commits.
5. Push the feature branch and open a pull request.
6. Review changes and ensure all CI checks pass.
7. Squash and merge the pull request.
8. Delete the merged feature branch.

## Continuous Integration

GitHub Actions automatically verifies pull requests and
pushes to `main`.

The CI pipeline performs:

- Java compilation
- Automated tests
- PostgreSQL integration tests
- Checkstyle verification
- SpotBugs static analysis
- JaCoCo coverage reporting
- Verification report publication

The `main` branch is protected by GitHub Rulesets,
requiring pull requests and successful CI checks before merging.

## Project Status

Under active development.

The application infrastructure, database connectivity,
migration management, and automated verification pipeline
are implemented.

Core auction house business functionality is being
developed incrementally.
