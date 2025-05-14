# QA Lab: Bug Tracker

A professional-grade QA Lab project designed to simulate real-world test architecture and automation practices. Built using **Spring Boot**, **PostgreSQL**, **Flyway**, **Docker**, **Testcontainers**, and **JaCoCo**, this application provides a hands-on platform for exploring advanced quality assurance methodologies.

---

## Features

- **Spring Boot** backend for a simple bug tracking system.
- **RESTful API** with endpoints for bug report creation and retrieval.
- **PostgreSQL** database with schema versioning via **Flyway**.
- **Unit tests** using JUnit & Spring Boot Test.
- **Integration tests** with Testcontainers and Maven Failsafe.
- **Test coverage** reporting via JaCoCo (merged from unit & integration tests).
- **GitLab CI/CD pipeline** with build, test, and coverage stages.

---

## Project Structure

src
├── main
│ ├── java/com/qalab/bugtracker
│ └── resources
│ └── db/migration # Flyway scripts
├── test
│ ├── java/com/qalab/bugtracker/qa/tests
│ │ ├── unit # Unit tests
│ │ └── integration # Integration tests
│ └── resources
└── Dockerfile / docker-compose.yml

---

## Run Locally

### Option 1: Docker Compose

```bash
docker-compose up --build

Access: http://localhost:8080

### Option 2: Maven Spring Boot

mvn spring-boot:run

Testing

Unit Tests
mvn test

Integration Tests
mvn verify

Code Coverage
Merged report available at:
target/site/jacoco/index.html

CI/CD Pipeline (GitLab)
Build Stage: Compile the app

Test Stage: Run unit and integration tests

Report Stage: Merge .exec files and generate JaCoCo report

All executed automatically on every push to main or in merge requests.

Technologies
Java 17

Spring Boot 3

PostgreSQL

Flyway

JUnit 5

Testcontainers

Docker

Maven + JaCoCo

GitLab CI/CD

License
MIT

Author
QA Lab by hmrad1984
Focused on continuous quality and modern test strategies.