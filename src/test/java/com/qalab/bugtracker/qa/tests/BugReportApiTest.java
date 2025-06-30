package com.qalab.bugtracker.qa.tests;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

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
import static org.mockito.Answers.valueOf;
import static io.restassured.module.jsv.JsonSchemaValidator.*;

import io.restassured.http.ContentType;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.InputStream;
import java.util.Optional;
import java.util.List;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.qalab.bugtracker.model.BugReport;
import com.qalab.bugtracker.qa.model.BugReportDTO;

import com.qalab.bugtracker.qa.model.BugReportTestCase;
import com.qalab.bugtracker.qa.utils.TestDataLoader;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static io.restassured.RestAssured.*;

import org.springframework.boot.test.mock.mockito.MockBean;
import com.qalab.bugtracker.repository.BugReportRepository;

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

        // =====================
        // GET Tests
        // =====================

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

        // =====================
        // POST Tests
        // =====================

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

        // =====================
        // Negative Tests
        // =====================

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

        // =====================
        // Parameterized Tests
        // =====================

        @ParameterizedTest
        @CsvSource({
                        "OPEN, , true",
                        ", HIGH, true",
                        "OPEN, HIGH, true",
                        ", , true"
        })
        void shouldFilterBugReports(String status, String severity, boolean expectResults) {
                RequestSpecification request = given();

                if (status != null && !status.isBlank()) {
                        request = request.queryParam("status", status);
                }
                if (severity != null && !severity.isBlank()) {
                        request = request.queryParam("severity", severity);
                }

                request.when()
                                .get("/api/bugs/filter")
                                .then()
                                .statusCode(200)
                                .body("size()", expectResults ? greaterThanOrEqualTo(0) : equalTo(0));
        }

        // =====================
        // Parameterized Tests from External JSON Data
        // =====================

        @Test
        void shouldCreateBugReportsFromJsonData() throws Exception {
                ObjectMapper objectMapper = new ObjectMapper();
                InputStream inputStream = getClass().getResourceAsStream("/testdata/bugreports.json");

                List<BugReportDTO> bugReports = objectMapper.readValue(
                                inputStream, new TypeReference<List<BugReportDTO>>() {
                                });

                for (BugReportDTO dto : bugReports) {
                        given()
                                        .contentType(ContentType.JSON)
                                        .body(dto)
                                        .when()
                                        .post("/api/bugs")
                                        .then()
                                        .statusCode(201)
                                        .body("title", equalTo(dto.getTitle()))
                                        .body("description", equalTo(dto.getDescription()))
                                        .body("status", equalTo(dto.getStatus()))
                                        .body("severity", equalTo(dto.getSeverity().toString()));
                }
        }

        // =====================
        // Parameterized Tests from External Data + expected results in JSON
        // =====================

        @ParameterizedTest(name = "{0}")
        @MethodSource("bugReportTestData")
        void shouldCreateBugReport(BugReportTestCase testCase) {
                BugReport bugReport = new BugReport();
                bugReport.setTitle(testCase.getTitle());
                bugReport.setDescription(testCase.getDescription());
                bugReport.setStatus(testCase.getStatus());
                bugReport.setSeverity(BugReport.Severity.valueOf(testCase.getSeverity()));

                var response = given()
                                .contentType(ContentType.JSON)
                                .body(bugReport)
                                .when()
                                .post("/api/bugs");
                if (testCase.getExpectedStatus() == 201) {
                        response.then()
                                        .statusCode(testCase.getExpectedStatus())
                                        .body(containsString(testCase.getExpectedResponseFragment()));
                } else if (testCase.getExpectedStatus() == 400) {
                        response.then()
                                        .statusCode(testCase.getExpectedStatus())
                                        .body(containsString(testCase.getExpectedResponseFragment()));
                }
        }

        static Stream<BugReportTestCase> bugReportTestData() throws Exception {
                ObjectMapper objectMapper = new ObjectMapper();
                InputStream is = BugReportApiTest.class.getResourceAsStream("/testdata/bug_report_test_cases.json");
                List<BugReportTestCase> testCases = objectMapper.readValue(is,
                                new TypeReference<List<BugReportTestCase>>() {
                                });
                return testCases.stream();
        }

}