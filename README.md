
# Vert.x Auction House

A reactive auction house backend built with Java, Vert.x,
and PostgreSQL.

## Tech Stack

- Java 21
- Vert.x 5
- Gradle (Kotlin DSL)
- JUnit 6

## Planned Features

- User authentication and authorization
- User and organization management
- Auction creation and management
- Bid placement and retrieval
- Auction clearing with price-time priority
- Real-time notifications via WebSocket
- PostgreSQL persistence

## Getting Started

### Prerequisites

- JDK 21

### Build

```bash
./gradlew clean build
```

### Run

```bash
./gradlew run
```

### Run Tests

```bash
./gradlew test
```

On Windows, use `.\gradlew.bat` instead of `./gradlew`.

### Test Coverage

Generate the JaCoCo coverage report:

```bash
./gradlew jacocoTestReport
```

The HTML report is available at:

`build/reports/jacoco/test/html/index.html`

Coverage and verification reports are also available
as artifacts in GitHub Actions workflow runs.


### Database Setup

The project uses PostgreSQL 17 with Docker Compose.

Create a local environment configuration from
`.env.example` and set a development password.

On Windows:

```powershell
Copy-Item .env.example .env
```

On Linux or macOS:

```bash
cp .env.example .env
```

Start PostgreSQL:

```bash
docker compose up -d
```

Verify the database container:

```bash
docker compose ps
```

Stop PostgreSQL:

```bash
docker compose down
```

Database credentials are configured through environment
variables. Do not commit the `.env` file.


## Current Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /health | Application health check |


## Development Workflow

### Git Hooks

Enable the repository's Git hooks after cloning:

```bash
git config --local core.hooksPath .githooks
```

The following checks run automatically:

| Hook | Verification |
|------|--------------|
| pre-commit | Checkstyle |
| commit-msg | Conventional Commit format |
| pre-push | Build, tests, and static analysis |

### Contributing

1. Create a feature branch from main.
2. Implement changes and write tests.
3. Use Conventional Commit messages.
4. Open a pull request.
5. Ensure all CI checks pass.
6. Squash and merge.
7. Delete the merged branch.


## Project Status

Under active development.
