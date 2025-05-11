package com.qalab.bugtracker.qa.tests;

import com.qalab.bugtracker.model.BugReport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class BugReportControllerIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @LocalServerPort
    private int port;

    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    void testGetAllBugReportsFromPreloadedData() {
        String baseUrl = "http://localhost:" + port + "/api/bugs";

        ResponseEntity<BugReport[]> response = restTemplate.getForEntity(baseUrl, BugReport[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        List<BugReport> bugReports = Arrays.asList(response.getBody());

        assertThat(bugReports).hasSize(3);

        assertThat(bugReports)
                .extracting(BugReport::getTitle)
                .containsExactlyInAnyOrder(
                        "Login button not working",
                        "Typo on homepage",
                        "Profile page crash");

        assertThat(bugReports)
                .extracting(BugReport::getSeverity)
                .isNotEmpty()
                .contains(BugReport.Severity.HIGH, BugReport.Severity.MEDIUM, BugReport.Severity.LOW);
    }
}
