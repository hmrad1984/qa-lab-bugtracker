# 🧪 QA Lab – Bug Tracker Sandbox

Welcome to the QA Lab — a clean, containerized playground for testing real-world quality assurance workflows in a modern Java + Spring Boot microservice environment.

This project simulates a **bug tracking system** and includes a fully automated backend setup designed to support:

- 🚀 Test Automation
- 🐳 Docker-based Environments
- 🧬 Flyway Database Migrations
- 🔍 Integration Testing with Testcontainers

---

## 📦 Tech Stack

| Layer         | Tool / Framework              |
|---------------|-------------------------------|
| Language      | Java 17                       |
| Backend       | Spring Boot                   |
| Database      | PostgreSQL (via Testcontainers in tests) |
| Migration     | Flyway                        |
| Build Tool    | Maven                         |
| Testing       | JUnit 5, Testcontainers, AssertJ |
| Containerization | Docker, Docker Compose     |

---

## 🧰 Key Features

- **Clean Architecture**: Built with separation of concerns in mind.
- **Flyway Integration**: DB migrations are version-controlled and automatically applied.
- **Testcontainers Support**: Integration tests run against real PostgreSQL containers.
- **CI-Ready Structure**: Ideal for building pipelines around.

---

## 🚀 Getting Started

### Clone the repo

```bash
git clone https://github.com/<your-username>/qalab-bugtracker.git
cd qalab-bugtracker
