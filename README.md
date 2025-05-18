# Bug Tracker QA Lab 🐛

A comprehensive QA testing project demonstrating best practices in test automation using Spring Boot, Testcontainers, and various testing frameworks.

## 🎯 Project Overview

This project serves as a QA laboratory for implementing and demonstrating different testing strategies in a Spring Boot application. It includes a bug tracking REST API with various test implementations showcasing:

- Unit Testing
- Integration Testing
- API Testing
- Container-based Testing
- Test Data Management

## 🛠 Tech Stack

- **Framework:** Spring Boot 3.x
- **Language:** Java 17
- **Database:** PostgreSQL
- **Testing Frameworks:**
  - JUnit Jupiter (JUnit 5)
  - Testcontainers
  - REST Assured
  - MockMvc
- **Build Tool:** Maven
- **Database Migration:** Flyway
- **Containerization:** Docker

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- Docker (for running Testcontainers)
- PostgreSQL (for local development)

### Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/qalab-bugtracker.git
   cd qalab-bugtracker
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the tests:
   ```bash
   mvn verify
   ```

## 🧪 Testing Strategy

### 1. Unit Tests
Located in `src/test/java/com/qalab/bugtracker/unit/`
- Controller unit tests with MockMvc
- Isolated testing with mocked dependencies
- Fast execution for quick feedback

### 2. Integration Tests
Located in `src/test/java/com/qalab/bugtracker/qa/tests/`
- End-to-end testing with real database
- Testcontainers for isolated database testing
- Full Spring context testing

### 3. API Tests
- REST Assured for API testing
- HTTP response validation
- JSON payload verification

### 4. Test Data Management
- Flyway migrations for test data
- Containerized database per test
- Isolated test environments

## 📁 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/qalab/bugtracker/
│   │       ├── controller/
│   │       ├── model/
│   │       └── repository/
│   └── resources/
│       └── application.properties
└── test/
    ├── java/
    │   └── com/qalab/bugtracker/
    │       ├── unit/
    │       │   └── controller/
    │       └── qa/
    │           └── tests/
    └── resources/
        ├── application-test.yml
        └── db/migration/
```

## 🔍 Test Examples

### Unit Test Example
```java
@WebMvcTest(BugReportController.class)
class BugReportControllerTest {
    @Test
    void shouldReturnListOfBugs() {
        // Test implementation
    }
}
```

### Integration Test Example
```java
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Testcontainers
class BugReportControllerIT {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>();
    
    @Test
    void testGetAllBugReports() {
        // Test implementation
    }
}
```

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## ✨ Acknowledgments

- Spring Boot Testing Documentation
- Testcontainers Documentation
- REST Assured Documentation
- JUnit 5 User Guide