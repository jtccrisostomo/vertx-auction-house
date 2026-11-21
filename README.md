
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

## Current Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /health | Application health check |

## Project Status

Under active development.
