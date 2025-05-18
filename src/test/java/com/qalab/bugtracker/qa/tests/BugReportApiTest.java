package com.qalab.bugtracker.qa.tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class BugReportApiTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.2")
            .withDatabaseName("bugtracker")
            .withUsername("qalab")
            .withPassword("qalab123");

    @LocalServerPort
    int port;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void shouldGetAllBugReports() {
        given()
                .when()
                .get("/api/bugs")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(0)); // Adjust as needed
    }

    @Test
    void shouldCreateNewBugReport() {
        String newBug = """
                {
                    "title": "UI not responsive",
                    "description": "Button click does not register",
                    "status": "OPEN",
                    "severity": "HIGH"
                }
                """;

        given()
                .header("Content-Type", "application/json")
                .body(newBug)
                .when()
                .post("/api/bugs")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("title", equalTo("UI not responsive"));
    }

    @Test
    void shouldRejectInvalidBugReport() {
        String invalidBug = """
                {
                    "title": "",
                    "description": "",
                    "status": "INVALID_STATUS",
                    "severity": "EXTREME"
                }
                """;

        given()
                .header("Content-Type", "application/json")
                .body(invalidBug)
                .when()
                .post("/api/bugs")
                .then()
                .statusCode(400); // Adjust depending on your validation setup
    }

    @Test
    void shouldReturnBugById() {
        int testBugId = 1; // Replace with a valid ID from your test data
        when()
                .get("/api/bugs/{id}", testBugId)
                .then()
                .statusCode(200)
                .body("id", equalTo(testBugId));
    }

}
