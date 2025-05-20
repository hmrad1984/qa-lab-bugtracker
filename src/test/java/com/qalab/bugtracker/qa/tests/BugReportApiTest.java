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
import static io.restassured.module.jsv.JsonSchemaValidator.*;

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

    @Test
    void shouldMatchBugReportSchema() {
        given()
                .when()
                .get("/api/bugs/1")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/bug-report-schema.json"));

    }

    @Test
    void shouldReturn404ForNonExistentBugReport() {
        given()
                .when()
                .get("/api/bugs/9999")
                .then()
                .statusCode(404);
    }

    @Test
    void shouldReturn400ForInvalidPostPayload() {
        String invalidJson = "{ \"title\": \"Missing fields\" }"; // description/status/severity are missing

        given()
                .contentType("application/json")
                .body(invalidJson)
                .when()
                .post("/api/bugs")
                .then()
                .statusCode(400);
    }

    @Test
    void shouldReturn405ForUnsupportedMethod() {
        given()
                .when()
                .patch("/api/bugs/1")
                .then()
                .statusCode(405);
    }

    @Test
    void shouldReturn415ForUnsupportedContentType() {
        given()
                .contentType("text/plain") // Invalid content type
                .body("Just some text")
                .when()
                .post("/api/bugs")
                .then()
                .statusCode(415);
    }

    @Test
    void shouldReturn400ForInvalidEnumValue() {
        String invalidEnumPayload = """
                {
                  "title": "Invalid severity",
                  "description": "Testing with wrong severity",
                  "status": "OPEN",
                  "severity": "CRITICAL_BROKEN"
                }
                """;

        given()
                .contentType("application/json")
                .body(invalidEnumPayload)
                .when()
                .post("/api/bugs")
                .then()
                .statusCode(400);
    }

    @Test
    void shouldIgnoreUnknownFieldsInPostPayload() {
        String payload = """
                {
                  "title": "Bug with extra field",
                  "description": "Extra field should be ignored",
                  "status": "OPEN",
                  "severity": "LOW",
                  "hackerMode": true
                }
                """;

        given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/api/bugs")
                .then()
                .statusCode(201)
                .body("title", equalTo("Bug with extra field"));
    }

    @Test
    void shouldRejectEmptyTitleInPostPayload() {
        String payload = """
                {
                  "title": "",
                  "description": "Missing title",
                  "status": "OPEN",
                  "severity": "MAJOR"
                }
                """;

        given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/api/bugs")
                .then()
                .statusCode(400);
    }

    @Test
    void shouldRejectNullDescriptionInPostPayload() {
        String payload = """
                {
                  "title": "Null desc",
                  "description": null,
                  "status": "OPEN",
                  "severity": "MAJOR"
                }
                """;

        given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/api/bugs")
                .then()
                .statusCode(400);
    }

    @Test
    void shouldRejectTooLongTitle() {
        String longTitle = "A".repeat(300); // Adjust based on your validation limit

        String payload = String.format("""
                {
                  "title": "%s",
                  "description": "Very long title test",
                  "status": "OPEN",
                  "severity": "MAJOR"
                }
                """, longTitle);

        given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/api/bugs")
                .then()
                .statusCode(400);
    }

}